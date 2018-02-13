package com.idealista.scraper.webdriver;

import java.util.concurrent.ConcurrentLinkedQueue;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.scraper.webdriver.WebDriverFactory.DriverType;
import com.idealista.scraper.webdriver.proxy.ProxyAdapter;
import com.idealista.scraper.webdriver.proxy.ProxyProvider;
import com.idealista.web.config.BaseScraperConfiguration;

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

    @Autowired
    private BaseScraperConfiguration scraperConfiguration;

    private ProxyAdapter proxy;

    private final ThreadLocal<Boolean> isWebDriverInitialized = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private final ThreadLocal<WebDriver> threadedWebDriver = new ThreadLocal<WebDriver>()
    {
        @Override
        protected WebDriver initialValue()
        {
            DriverType type = DriverType.fromString(scraperConfiguration.getDriverType());
            WebDriver driver = null;
            int retryCount = 5;
            if (scraperConfiguration.isUseProxy())
            {
                synchronized (this)
                {
                    if (!localProxy.get().equals(proxy))
                    {
                        proxy = proxyProvider.getNextWorkingProxy();
                    }
                    localProxy.set(proxy);
                }
                while (retryCount > 0)
                {
                    try
                    {
                        driver = webDriverFactory.create(proxy, type);
                        break;
                    }
                    catch (WebDriverException e)
                    {
                        LOGGER.error("Failed to start the browser, retrying#{}: {}", --retryCount, e);
                    }
                }

            }
            else
            {
                while (retryCount > 0)
                {
                    try
                    {
                        driver = webDriverFactory.create(type);
                        break;
                    }
                    catch (WebDriverException e)
                    {
                        LOGGER.error("Failed to start the browser, retrying#{}: {}", --retryCount, e);
                    }
                }
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
        webDriverFactory.shutDown();
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
        if (scraperConfiguration.isUseProxy())
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
