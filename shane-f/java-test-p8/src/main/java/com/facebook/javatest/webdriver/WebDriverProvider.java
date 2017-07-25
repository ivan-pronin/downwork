package com.facebook.javatest.webdriver;

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
        return instance;
    }

    private static WebDriver startDriver()
    {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("-incognito");
        options.addArguments("test-type");
        options.addArguments("start-maximized");
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
