package com.idealista.scraper.proxy;

import com.idealista.scraper.ui.ClickActions;
import com.idealista.scraper.ui.SearchActions;
import com.idealista.scraper.util.FileUtils;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ProxyFetcherTests
{
    private static final String FREE_PROXY_CZ = "http://free-proxy.cz/en/proxylist/main/date/1";
    
    @Test
    public void testFetchProxies() throws Exception
    {
        WebDriver driver = new ChromeDriver();
        driver.get(FREE_PROXY_CZ);
        Set<String> proxies = new HashSet<>();
        
        for (int i = 0; i< 9; i ++)
        {
            proxies.addAll(getProxiesFromNextPage(driver));
        }
        System.out.println("Total new proxies: " + proxies.size());
        driver.quit();
    }

    private Set<String> getProxiesFromNextPage(WebDriver driver) throws InterruptedException
    {
        SearchActions searchActions = new SearchActions();
        ClickActions clickActions = new ClickActions();
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
        Set<String> proxies = new HashSet<>();
        List<WebElement> exportButton = searchActions.findElementsById("clickexport");
        clickActions.click(exportButton);
        
        WebElement proxiesBlock = searchActions.waitForElement(By.id("zkzk"), 5);
        if (proxiesBlock != null)
        {
            String text = proxiesBlock.getText();
            Set<String> lines = FileUtils.readStringToLines(text);
            System.out.println(lines);
            System.out.println("Size: " + lines.size());
            proxies.addAll(lines);
            clickActions.click(exportButton);
        }
        List<WebElement> nextButton = searchActions.findElementsByXpath("//a[contains(.,'Next')]");
        clickActions.click(nextButton);
        int seconds = 0;
        synchronized (this)
        {
            System.out.println("Waiting for " + seconds + " seconds");
            //this.wait(seconds * 1000);
        }
        return proxies;
    }
}
