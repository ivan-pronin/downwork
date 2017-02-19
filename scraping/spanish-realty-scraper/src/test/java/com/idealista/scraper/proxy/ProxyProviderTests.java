package com.idealista.scraper.proxy;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class ProxyProviderTests
{
    //@Test
    public void testGetNextWorkingProxy()
    {
        ProxyProvider provider = new ProxyProvider();
        provider.getNextWorkingProxy();
    }
    
    @Test
    public void testJbrowserProxy()
    {
        WebDriverProvider provider = new WebDriverProvider();
        WebDriver driver = provider.get();
        try
        {
            driver.get("https://www.idealista.com/venta-viviendas/barcelona-provincia/");
            System.out.println("Current page: " + driver.getCurrentUrl());
            WebElement element = driver.findElement(By.id("no-login-user-bar"));
            System.out.println(element);
        }
        catch (FailingHttpStatusCodeException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
