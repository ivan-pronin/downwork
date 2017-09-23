package com.idealista.scraper.webdriver.proxy;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.scraper.webdriver.WebDriverProvider;

@Component
public class ProxyMonitor
{
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private WebDriverProvider webDriverProvider;

    public WebDriver checkForVerificationAndRestartDriver(WebDriver driver)
    {
        if (ifVerificationAppered(driver))
        {
            LOGGER.info("Verification page is opened, restarting driver");
            return restartDriver();
        }
        return driver;
    }

    public boolean ifVerificationAppered(WebDriver driver)
    {
        return driver.getCurrentUrl().contains("checkvalidation");
    }

    public WebDriver restartDriver()
    {
        webDriverProvider.end();
        return webDriverProvider.get();
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }
}
