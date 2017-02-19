package com.idealista.scraper.proxy;

import com.idealista.scraper.webdriver.WebDriverProvider;

import org.openqa.selenium.WebDriver;

public class ProxyMonitor
{
    public WebDriver checkForVerificationAndRestartDriver(WebDriver driver, WebDriverProvider webDriverProvider)
    {
        if (ifVerificationAppered(driver))
        {
            return restartDriver(webDriverProvider);
        }
        return driver;
    }

    public boolean ifVerificationAppered(WebDriver driver)
    {
        return driver.getCurrentUrl().contains("checkvalidation");
    }

    public WebDriver restartDriver(WebDriverProvider webDriverProvider)
    {
        webDriverProvider.end();
        return webDriverProvider.get();
    }
}
