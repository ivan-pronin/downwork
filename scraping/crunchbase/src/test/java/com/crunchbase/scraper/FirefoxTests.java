package com.crunchbase.scraper;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import com.crunchbase.scraper.service.CrunchbaseScraperService;
import com.crunchbase.scraper.ui.SearchActions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import io.netty.handler.codec.http.HttpHeaders;
import io.netty.handler.codec.http.HttpRequest;
import io.netty.handler.codec.http.HttpResponse;
import net.lightbody.bmp.BrowserMobProxy;
import net.lightbody.bmp.BrowserMobProxyServer;
import net.lightbody.bmp.client.ClientUtil;
import net.lightbody.bmp.filters.RequestFilter;
import net.lightbody.bmp.util.HttpMessageContents;
import net.lightbody.bmp.util.HttpMessageInfo;

public class FirefoxTests
{

    private static final Logger LOGGER = LogManager.getLogger(FirefoxTests.class);

    @Test
    public void testName() throws Exception
    {
        int maxThreads = 4;
        ExecutorService executor = Executors.newFixedThreadPool(maxThreads);
        for (int i = 0; i < maxThreads; i++)
        {
            executor.submit(new ThreadedFF());
        }
        executor.awaitTermination(60, TimeUnit.SECONDS);
        executor.shutdown();
    }

    private class ThreadedFF implements Runnable
    {
        @Override
        public void run()
        {
            WebDriver driver = new FirefoxDriver();
            LOGGER.info("Browser started");
            driver.get(CrunchbaseScraperService.CRUNCHBASE_COM);
            SearchActions actions = new SearchActions();
            actions.setWebDriver(driver);
            WebElement element = actions.waitForElement(By.xpath("//h2[text()='Trending on Crunchbase']"), 60);
            Assert.assertNotNull("Page loaded!", element);
        }
    }

    private class FfThreaded implements Runnable
    {

        @Override
        public void run()
        {
            BrowserMobProxy proxy = new BrowserMobProxyServer();
            Map<String, String> headers = new HashMap<>();
            // headers.put("Host", "www.crunchbase.com");
            // headers.put("Connection", "keep-alive");
            // headers.put("Upgrade-Insecure-Requests", "1");
            // headers.put("User-Agent",
            // "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87
            // Safari/537.36");
            headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            headers.put("Accept-Encoding", "gzip, deflate, sdch, br");
            headers.put("Accept-Language", "en-US,en;q=0.8,ru;q=0.6,pl;q=0.4");
            headers.remove("User-Agent");
            // headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
            // headers.put("Accept-Language", "en-US,en;q=0.8,ru;q=0.6,pl;q=0.4");
            // proxy.addHeaders(headers);
            proxy.addHeaders(headers);
            // addRequestFilter(proxy);

            proxy.setTrustAllServers(true);
            proxy.start(5656);

            Proxy seleniumProxy = ClientUtil.createSeleniumProxy(proxy);
            DesiredCapabilities capabilities = new DesiredCapabilities();
            capabilities.setCapability(CapabilityType.PROXY, seleniumProxy);

            WebDriver driver = new ChromeDriver();
            driver.manage().timeouts().pageLoadTimeout(20, TimeUnit.MINUTES);
            LOGGER.info("Browser started");

            for (int i = 0; i < 10; i++)
            {
                driver.get(CrunchbaseScraperService.CRUNCHBASE_COM);
                SearchActions actions = new SearchActions();
                actions.setWebDriver(driver);
                WebElement element = actions.waitForElement(By.xpath("//h2[text()='Trending on Crunchbase']"), 6000);
                Assert.assertNotNull("Page loaded!", element);
            }
        }

        private void addRequestFilter(BrowserMobProxy proxy)
        {
            proxy.addRequestFilter(new RequestFilter()
            {
                @Override
                public HttpResponse filterRequest(HttpRequest request, HttpMessageContents contents,
                        HttpMessageInfo messageInfo)
                {
                    HttpHeaders headers = request.headers();
                    System.out.println();
                    System.out.println("REquest: " + request.getUri());
                    System.out.println();

                    headers.clear();
                    headers.set("Host", "www.crunchbase.com");
                    headers.set("Connection", "keep-alive");
                    headers.set("Upgrade-Insecure-Requests", "1");
                    headers.set("User-Agent",
                            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
                    headers.set("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,*/*;q=0.8");
                    headers.set("Accept-Encoding", "gzip, deflate, sdch, br");
                    headers.set("Accept-Language", "en-US,en;q=0.8,ru;q=0.6,pl;q=0.4");
                    // headers.remove("Accept");
                    // headers.remove("Accept-Encoding");
                    // headers.remove("User-Agent");
                    System.out.println("Printing headers:");
                    System.out.println(headers.entries());
                    return null;
                }
            });

        }
    }
}
