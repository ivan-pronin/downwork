package com.cactusglobal.whiteboard.util;

import com.cactusglobal.whiteboard.WebDriverProvider;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.List;

public final class WebDriverUtil
{
    private static final Logger LOGGER = LogManager.getLogger(WebDriverUtil.class);

    public static void waitForPageToLoad(WebDriver driver)
    {
        LOGGER.info("Waiting for page to load: {}", driver.getCurrentUrl());
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 60)
        {
            String value = String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"));
            if (value.equals("complete"))
            {
                LOGGER.info("Page loaded");
                return;
            }
        }
        LOGGER.error("Failed to load the page due to timeout");
    }

    public static void openNewTab(WebDriver driver, String url)
    {
        String script = "window.open('%s','_blank');";
        ((JavascriptExecutor) driver).executeScript(String.format(script, url));
        waitForPageToLoad(driver);
    }

    public static void switchToOtherTab(WebDriver driver, List<String> prevoiusTabs)
    {
        for (String tab : driver.getWindowHandles())
        {
            if (!prevoiusTabs.contains(tab))
            {
                driver.switchTo().window(tab);
            }
        }
    }

    public static void takeScreenShot()
    {
        String fileName = DateTimeUtil.getTimeStamp() + ".png";
        LOGGER.info("Taking screenshot with name: {}", fileName);
        File scrFile = ((TakesScreenshot) WebDriverProvider.getDriverInstance()).getScreenshotAs(OutputType.FILE);
        try
        {
            File destFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(scrFile, destFile);
            LOGGER.info("Screenshot saved successfully to path: {}", destFile.getAbsolutePath());
        }
        catch (IOException e)
        {
            LOGGER.error("Error while copying screenshot file: {}", e);
        }
    }
}
