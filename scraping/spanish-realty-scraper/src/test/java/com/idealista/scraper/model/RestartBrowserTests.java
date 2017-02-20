package com.idealista.scraper.model;

import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class RestartBrowserTests
{
    
    private static final Logger LOGGER = LogManager.getLogger(RestartBrowserTests.class);
    
    @Test
    public void testCheckRestartingBrowser()
    {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        int iterations = 22;
        WebDriverProvider provider = new WebDriverProvider();
        
        for (int i = 0; i < iterations ; i++)
        {
            executor.submit(() -> 
            {
              WebDriver driver = provider.get();
              driver.navigate().to("http://www.tut.by");
              LOGGER.info("Window handle: {}", driver.getWindowHandle());
            });
        }
        executor.shutdown();
        try
        {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
