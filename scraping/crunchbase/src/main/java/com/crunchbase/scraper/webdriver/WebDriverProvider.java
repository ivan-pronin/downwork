package com.crunchbase.scraper.webdriver;

import com.crunchbase.scraper.webdriver.WebDriverFactory.DriverType;
import com.crunchbase.scraper.webdriver.proxy.ProxyAdapter;
import com.crunchbase.scraper.webdriver.proxy.ProxyProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

@Component
public class WebDriverProvider implements IWebDriverProvider
{
    private static final Logger LOGGER = LogManager.getLogger(WebDriverProvider.class);
    private static volatile int driverStartCounter;

    private final ConcurrentLinkedQueue<WebDriver> webDrivers = new ConcurrentLinkedQueue<>();
    private final ThreadLocal<ProxyAdapter> localProxy = ThreadLocal.withInitial(ProxyAdapter::new);

    @Autowired
    private WebDriverFactory webDriverFactory;

    @Autowired
    private ProxyProvider proxyProvider;

    @Value("${useProxy}")
    private boolean useProxy;

    @Value("${driverType}")
    private String driverType;

    private ProxyAdapter proxy;

    private final ThreadLocal<Boolean> isWebDriverInitialized = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private final ThreadLocal<WebDriver> threadedWebDriver = new ThreadLocal<WebDriver>()
    {
        @Override
        protected WebDriver initialValue()
        {
            DriverType type = DriverType.fromString(driverType);
            WebDriver driver = null;
            if (useProxy)
            {
                synchronized (this)
                {
                    if (localProxy.get().equals(proxy))
                    {
                        proxy = proxyProvider.getNextWorkingProxy();
                    }
                    localProxy.set(proxy);
                }
                driver = webDriverFactory.create(proxy, type);
            }
            else
            {
                driver = webDriverFactory.create(type);
            }
            webDrivers.add(driver);
            LOGGER.info("Started new WebDriver instance. Start counter: {}", getStartCounterIndex());
            return driver;
        }
    };

    @Override
    public void destroy()
    {
        for (WebDriver webDriver : webDrivers)
        {
            webDriver.quit();
        }
    }

    @Override
    public void end()
    {
        if (isWebDriverInitialized())
        {
            WebDriver webDriver = threadedWebDriver.get();
            try
            {
                webDriver.quit();
            }
            finally
            {
                webDrivers.remove(webDriver);
                reset();
            }
        }
        else
        {
            reset();
        }
    }

    @Override
    public WebDriver get()
    {
        WebDriver webDriver = threadedWebDriver.get();
        isWebDriverInitialized.set(Boolean.TRUE);
        return webDriver;
    }

    @PostConstruct
    private void init()
    {
        if (useProxy)
        {
            proxy = proxyProvider.getNextWorkingProxy();
        }
    }

    @Override
    public boolean isWebDriverInitialized()
    {
        return isWebDriverInitialized.get().booleanValue();
    }

    public void setProxyProvider(ProxyProvider proxyProvider)
    {
        this.proxyProvider = proxyProvider;
    }

    public void setWebDriverFactory(WebDriverFactory webDriverFactory)
    {
        this.webDriverFactory = webDriverFactory;
    }

    private int getStartCounterIndex()
    {
        return ++driverStartCounter;
    }

    private void reset()
    {
        threadedWebDriver.remove();
        isWebDriverInitialized.set(Boolean.FALSE);
    }
}
