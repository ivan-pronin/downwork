package com.upwork.ivan.pronin;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
        ChromeOptions options = new ChromeOptions();
        options.addArguments("-incognito");
        DesiredCapabilities capabilities = DesiredCapabilities.chrome();
        capabilities.setCapability(ChromeOptions.CAPABILITY, options);
        instance = new ChromeDriver(capabilities);
        return instance;
    }

    public static void stopDriver()
    {
        instance.quit();
        instance = null;
    }
}
