package com.idealista.scraper.driver;

import java.util.Random;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.idealista.scraper.util.PropertiesLoader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class TestWebDriverProvider
{
    
    private static final Logger LOGGER = LogManager.getLogger(TestWebDriverProvider.class);

    private final ConcurrentLinkedQueue<WebDriver> webDrivers = new ConcurrentLinkedQueue<>();
    private final ThreadLocal<Integer> localProxy = ThreadLocal.withInitial(() -> new Integer(0));
    private static int proxyMock;

    private final ThreadLocal<Boolean> isWebDriverInitialized = new ThreadLocal<Boolean>()
    {
        @Override
        protected Boolean initialValue()
        {
            return Boolean.FALSE;
        }
    };

    private final ThreadLocal<WebDriver> threadedWebDriver = new ThreadLocal<WebDriver>()
    {
        @Override
        protected WebDriver initialValue()
        {
            WebDriver driver = null;
            if (Boolean.parseBoolean(PropertiesLoader.getProperties().getProperty("useProxy")))
            {
                if (localProxy.get().intValue() == proxyMock)
                {
                    proxyMock = new Random().nextInt(100000);
                    LOGGER.info("Generating NEW PROXY!. NEW proxy = {}", proxyMock);
                }
                localProxy.set(proxyMock);
                driver = new ChromeDriver();
            }
            else
            {
                driver = new ChromeDriver();
            }
            webDrivers.add(driver);
            return driver;
        }
    };

    public WebDriver get()
    {
        WebDriver webDriver = threadedWebDriver.get();
        isWebDriverInitialized.set(Boolean.TRUE);
        return webDriver;
    }

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

    public boolean isWebDriverInitialized()
    {
        return isWebDriverInitialized.get().booleanValue();
    }

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

    public static int getProxyMock()
    {
        return proxyMock;
    }

    public ThreadLocal<Integer> getLocalProxy()
    {
        return localProxy;
    }
}
