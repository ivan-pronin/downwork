package com.idealista.scraper.proxy;

import static org.junit.Assert.*;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.idealista.scraper.webdriver.WebDriverFactory;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.WebDriverFactory.DriverType;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class ProxyProviderTests
{
    // @Test
    public void testGetNextWorkingProxy()
    {
        ProxyProvider provider = new ProxyProvider();
        provider.getNextWorkingProxy();
    }

    // @Test
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
    
    @Test
    public void testIndex() throws Exception
    {
        checkProxy(2);
    }

    private void checkProxy(int returnIndex)
    {
        WebDriverFactory webDriverFactory = new WebDriverFactory();
        WebDriver driver = webDriverFactory.create(new ProxyAdapter(), DriverType.CHROME);
        try
        {
            driver.navigate().to("http://www.tut.by");
            switch (returnIndex)
            {
                case 1:
                    System.out.println("Returning TRUE");
                    return;
                case 2:
                    System.out.println("Throwint exception");
                    throw new WebDriverException();
                default:
                    break;
            }
        }
        catch (WebDriverException e)
        {
            System.out.println("CATCH block");
        }
        finally
        {
            System.out.println("Finally block");
            driver.quit();
        }
    }
}
