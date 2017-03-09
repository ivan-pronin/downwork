package com.idealista.scraper.driver;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.Temporal;

import junit.framework.TestCase;

@RunWith(BlockJUnit4ClassRunner.class)
public class ChromeServiceTests extends TestCase
{
    private static final String IDEALISTA_COM = "http://www.idealista.com";
    private static ChromeDriverService service;
    private WebDriver driver;

    @BeforeClass
    public static void createAndStartService() throws IOException
    {
        service = new ChromeDriverService.Builder().usingAnyFreePort().build();
        service.start();
    }

    @AfterClass
    public static void createAndStopService()
    {
        service.stop();
    }

    @Before
    public void createDriver()
    {
        driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
    }

    @After
    public void quitDriver()
    {
        System.out.println("Quitting driver");
        driver.quit();
    }

    @Test
    public void testStartServiceInstances() throws Exception
    {
        Instant start = Instant.now();
        for (int i = 0; i < 10; i++)
        {
            testGoogleSearch();
        }
        printExecutionTime(start);
    }

    // @Test
    public void testStartDriverInstances() throws Exception
    {
        Instant start = Instant.now();
        for (int i = 0; i < 10; i++)
        {
            WebDriver driver = new ChromeDriver();
            driver.navigate().to(IDEALISTA_COM);
            System.out.println("Cookies: " + driver.manage().getCookies());
            driver.quit();
            // driver.close();
        }
        printExecutionTime(start);
    }

    private void printExecutionTime(Temporal startTime)
    {
        System.out.println("Total time taken in seconds: " + Duration.between(startTime, Instant.now()).getSeconds());
    }

    private void testGoogleSearch()
    {
        driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
        driver.get(IDEALISTA_COM);
        System.out.println("Cookies: " + driver.manage().getCookies());
        driver.quit();
    }
}
