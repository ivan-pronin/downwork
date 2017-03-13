package com.idealista.scraper.util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;

public final class WebDriverUtil
{
    private static final Logger LOGGER = LogManager.getLogger(WebDriverUtil.class);

    public static void takeScreenShot(String message, WebDriver driver)
    {
        String fileName = DateTimeUtil.getTimeStamp() + ".png";
        LOGGER.info("{}. Taking screenshot with name: {}", message, fileName);
        try
        {
            File scrFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            File destFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(scrFile, destFile);
            LOGGER.info("Screenshot saved successfully to path: {}", destFile.getAbsolutePath());
        }
        catch (WebDriverException | IOException e)
        {
            LOGGER.error("Error while taking screenshot: {}", e);
        }
    }
}
