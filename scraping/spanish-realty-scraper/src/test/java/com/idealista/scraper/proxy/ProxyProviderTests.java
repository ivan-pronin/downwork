package com.idealista.scraper.proxy;

import static org.junit.Assert.*;

import com.gargoylesoftware.htmlunit.FailingHttpStatusCodeException;
import com.idealista.scraper.webdriver.WebDriverFactory;
import com.idealista.scraper.webdriver.WebDriverFactory.DriverType;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyAdapter;
import com.idealista.scraper.webdriver.proxy.ProxyProvider;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.Proxy.ProxyType;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

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

    // @Test
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
    
    @Test
    public void testHostProxy() throws Exception
    {
        String host = "bgproxy.site";
        int port = 8080;
        Proxy proxy = new Proxy().setHttpProxy(host).setSslProxy(host).setProxyType(ProxyType.MANUAL);
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.PROXY, proxy);
        WebDriver driver = new ChromeDriver(cap);
        String url = "http://www.tut.by";
        driver.navigate().to(url);
        System.out.println(driver.getCurrentUrl());
    }
}
