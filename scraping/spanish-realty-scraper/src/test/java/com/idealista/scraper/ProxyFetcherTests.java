package com.idealista.scraper;

import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyFetcher;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class ProxyFetcherTests
{
    @Autowired
    private ProxyFetcher proxyFetcher;
    
    @Autowired
    private WebDriverProvider webDriverProvider;

    @Test
    public void testName() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        proxyFetcher.setDriver(driver);
        Set<String> proxies = proxyFetcher.fetchProxies();
        System.out.println(proxies.size());
    }
}
