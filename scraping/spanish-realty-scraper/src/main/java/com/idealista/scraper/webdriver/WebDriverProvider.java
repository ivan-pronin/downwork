package com.idealista.scraper.webdriver;

import com.idealista.scraper.proxy.ProxyAdapter;
import com.idealista.scraper.proxy.ProxyProvider;
import com.idealista.scraper.util.PropertiesLoader;
import com.idealista.scraper.webdriver.WebDriverFactory.DriverType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ConcurrentLinkedQueue;

public class WebDriverProvider implements IWebDriverProvider
{
    private static final Logger LOGGER = LogManager.getLogger(WebDriverProvider.class);
    private static volatile int driverStartCounter;
    
    private final ConcurrentLinkedQueue<WebDriver> webDrivers = new ConcurrentLinkedQueue<>();
    private final ThreadLocal<ProxyAdapter> localProxy = ThreadLocal.withInitial(ProxyAdapter::new);

    private WebDriverFactory webDriverFactory = new WebDriverFactory();
    private ProxyProvider proxyProvider = new ProxyProvider();
    private ProxyAdapter proxy;

    public WebDriverProvider()
    {
        if (useProxy())
        {
            proxy = proxyProvider.getNextWorkingProxy();
        }
    }

    private final ThreadLocal<Boolean> isWebDriverInitialized = ThreadLocal.withInitial(() -> Boolean.FALSE);

    private final ThreadLocal<WebDriver> threadedWebDriver = new ThreadLocal<WebDriver>()
    {
        @Override
        protected WebDriver initialValue()
        {
            String driverType = PropertiesLoader.getProperties().getProperty("driverType");
            DriverType type = DriverType.fromString(driverType);
            WebDriver driver = null;
            if (useProxy())
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
    public WebDriver get()
    {
        WebDriver webDriver = threadedWebDriver.get();
        isWebDriverInitialized.set(Boolean.TRUE);
        return webDriver;
    }

    private int getStartCounterIndex()
    {
        return ++driverStartCounter;
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
    public boolean isWebDriverInitialized()
    {
        return isWebDriverInitialized.get().booleanValue();
    }

    @Override
    public void destroy()
    {
        for (WebDriver webDriver : webDrivers)
        {
            webDriver.quit();
        }
    }

    private void reset()
    {
        threadedWebDriver.remove();
        isWebDriverInitialized.set(Boolean.FALSE);
    }

    private boolean useProxy()
    {
        return Boolean.parseBoolean(PropertiesLoader.getProperties().getProperty("useProxy"));
    }
}
