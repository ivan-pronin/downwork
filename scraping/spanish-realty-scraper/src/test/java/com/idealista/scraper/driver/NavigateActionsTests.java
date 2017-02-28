package com.idealista.scraper.driver;

import static org.junit.Assert.*;

import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.NavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.net.URL;

public class NavigateActionsTests
{
    //@Test
    public void testName() throws Exception
    {
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        INavigateActions nactions = new NavigateActions(webDriverProvider);
        nactions.get(new URL("http://www.onliner.by"));
        nactions.get(new URL("http://www.idealista.com"));
        webDriverProvider.destroy();
    }
    
    //@Test
    public void testCheckProxy() throws Exception
    {
        String proxyAddr = "104.196.52.215:80";
        Proxy proxy = new Proxy();
        proxy.setHttpProxy(proxyAddr).setSslProxy(proxyAddr);
        DesiredCapabilities caps = DesiredCapabilities.chrome();
        caps.setCapability(CapabilityType.PROXY, proxy);
        WebDriver driver = new ChromeDriver(caps);
        String url = "http://www.idealista.com";
        driver.navigate().to(url);
        WebElement error = driver.findElement(By.xpath("//div[@class='error-code']"));
        System.out.println(error.isDisplayed());
        System.out.println(error.getText());
    }
    
    @Test
    public void testProxyConnection() throws Exception
    {
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        INavigateActions actions = new NavigateActions(webDriverProvider);
        URL url = new URL("http://www.idealista.com");
        actions.get(url);
    }
}
