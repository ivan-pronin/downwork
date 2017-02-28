package com.idealista.scraper.proxy;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Set;

public class ProxyFetcherTests
{
    @Test
    public void testFetchProxies() throws Exception
    {
        WebDriver driver = new ChromeDriver();
        ProxyFetcher proxyFetcher = new ProxyFetcher(driver);
        Set<String> proxies = proxyFetcher.fetchProxies();
        System.out.println("Size: " + proxies.size());
        System.out.println(proxies);
        driver.quit();
    }
}
