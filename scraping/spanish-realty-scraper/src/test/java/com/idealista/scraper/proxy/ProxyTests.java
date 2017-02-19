package com.idealista.scraper.proxy;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.idealista.scraper.driver.TestWebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

public class ProxyTests
{

    private static final Logger LOGGER = LogManager.getLogger(ProxyTests.class);

    // @Test
    public void testRegexp()
    {
        List<String> invalidAddresses = Arrays.asList("abc", "1.0.0.1 8080", "127.0.0.1 : 9000");
        List<String> validAddresses = Arrays.asList("100.0.0.1:9999", "127.200.200.255:65000", "1.1.1.1:112");
        invalidAddresses.forEach(e -> Assert.assertFalse(matches(e)));
        validAddresses.forEach(e -> Assert.assertTrue(matches(e)));
    }

    // @Test
    public void testName()
    {
        LOGGER.info("TEST");
    }

    @Test
    public void testSynchronizedProxyChanges() throws InterruptedException
    {
        TestWebDriverProvider webDriverProvider = new TestWebDriverProvider();
        ExecutorService executor = Executors.newFixedThreadPool(4);
        int maxIterations = 40;
        for (int i = 0; i < maxIterations; i++)
        {
            executor.execute(new ProxyChangeTask(webDriverProvider));
        }
        executor.shutdown();
        executor.awaitTermination(120, TimeUnit.SECONDS);
        webDriverProvider.destroy();
    }

    private boolean matches(String text)
    {
        return ProxyProvider.VALID_PROXY_ADDRESS_PATTERN.matcher(text).matches();
    }

    private class ProxyChangeTask implements Runnable
    {
        private TestWebDriverProvider webDriverProvider;
        private TestProxyMonitor proxyMonitor = new TestProxyMonitor();

        public ProxyChangeTask(TestWebDriverProvider webDriverProvider)
        {
            this.webDriverProvider = webDriverProvider;
        }

        @Override
        public void run()
        {
            LOGGER.info("ProxyChangeTask is starting to work");
            WebDriver driver = webDriverProvider.get();
            int diceRoll = new Random().nextInt(4);
            URL pageUrl = null;
            try
            {
                pageUrl = new URL("http://google.com");
            }
            catch (MalformedURLException e1)
            {
                e1.printStackTrace();
            }
            if (diceRoll == 0)
            {
                // simulating validation appear at random
                try
                {
                    LOGGER.info("Dice is 0!");
                    pageUrl = new URL("http://google.com/verification");
                }
                catch (MalformedURLException e)
                {
                    e.printStackTrace();
                }
            }
            driver.navigate().to(pageUrl);
            synchronized (this)
            {
                driver = proxyMonitor.checkForVerificationAndSwap(driver, webDriverProvider);
            }
        }
    }

    private class TestProxyMonitor
    {

        private final Logger LOGGER = LogManager.getLogger(ProxyTests.TestProxyMonitor.class);

        public WebDriver checkForVerificationAndSwap(WebDriver driver, TestWebDriverProvider webDriverProvider)
        {
            if (driver.getCurrentUrl().contains("verification"))
            {
                LOGGER.info("Restarting webdriverProvider's driver instance");
                LOGGER.info("Local proxy value before restart:" + webDriverProvider.getLocalProxy().get());
                webDriverProvider.end();
                driver = webDriverProvider.get();
                LOGGER.info("Current proxyMock value: {}", TestWebDriverProvider.getProxyMock());
                LOGGER.info("Local proxy value after restart:" + webDriverProvider.getLocalProxy().get());
            }
            return driver;
        }
    }
}
