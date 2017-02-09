package com.cactusglobal.whiteboard.util;

import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

public final class WebDriverUtil
{
    public static void waitForPageToLoad(WebDriver driver)
    {
        System.out.println("Waiting for page to load: " + driver.getCurrentUrl());
        long startTime = System.currentTimeMillis();
        while (System.currentTimeMillis() - startTime < 60)
        {
            String value = String.valueOf(((JavascriptExecutor) driver).executeScript("return document.readyState"));
            if (value.equals("complete"))
            {
                System.out.println("Page loaded");
                return;
            }
        }
        System.out.println("Failed to load the page");
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
}
