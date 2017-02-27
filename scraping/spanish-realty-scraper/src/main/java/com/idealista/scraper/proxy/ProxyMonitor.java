package com.idealista.scraper.proxy;

import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class ProxyMonitor
{
    private static final Logger LOGGER = LogManager.getLogger();
    
    public WebDriver checkForVerificationAndRestartDriver(WebDriver driver, WebDriverProvider webDriverProvider)
    {
        if (ifVerificationAppered(driver))
        {
            LOGGER.info("Verification page is opened, restarting driver");
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
