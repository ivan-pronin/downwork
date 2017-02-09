package com.cactusglobal.whiteboard;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverProvider
{
    private static WebDriver instance;

    public static WebDriver getDriverInstance()
    {
        if (instance == null)
        {
            instance = startDriver();
        }
        instance.manage().window().maximize();
        return instance;
    }

    private static WebDriver startDriver()
    {
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        instance = new ChromeDriver(capabilities);
        return instance;
    }

    public static void stopDriver()
    {
        instance.quit();
        instance = null;
    }
}
