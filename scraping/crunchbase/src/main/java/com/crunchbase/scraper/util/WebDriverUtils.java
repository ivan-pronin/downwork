package com.crunchbase.scraper.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

public final class WebDriverUtils
{
    private static final Logger LOGGER = LogManager.getLogger(WebDriverUtils.class);

    public static boolean waitForJSToLoad(WebDriver driver)
    {
        int timeOutInSeconds = 60;
        WebDriverWait wait = new WebDriverWait(driver, timeOutInSeconds);
        LOGGER.debug("Start waiting for JS to load ...");

        // wait for jQuery to load
        ExpectedCondition<Boolean> jQueryLoad = new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                try
                {
                    return ((Long) ((JavascriptExecutor) driver).executeScript("return jQuery.active") == 0);
                }
                catch (Exception e)
                {
                    return true;
                }
            }
        };

        // wait for Javascript to load
        ExpectedCondition<Boolean> jsLoad = new ExpectedCondition<Boolean>()
        {
            @Override
            public Boolean apply(WebDriver driver)
            {
                return ((JavascriptExecutor) driver).executeScript("return document.readyState").toString()
                        .equals("complete");
            }
        };
        boolean jsLoaded = wait.until(jQueryLoad) && wait.until(jsLoad);
        if (jsLoaded)
        {
            LOGGER.debug("JS loaded");
        }
        else
        {
            LOGGER.debug("JS didn't load in specified <{}> seconds time ...", timeOutInSeconds);
        }
        return jsLoaded;
    }

    public static void waitForPageLoad(WebDriver driver)
    {
        LOGGER.debug("Waiting for page to load: {}", driver.getCurrentUrl());
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 60 * 1000)
        {
            String value = String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"));

            if (value.equals("complete"))
            {
                LOGGER.debug("Page loaded");
                return;
            }
        }
        LOGGER.error("Failed to load the page");
    }
}
