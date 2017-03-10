package com.idealista.scraper.webdriver;

import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;

@Component
public class NavigateActions implements INavigateActions
{
    private static final Logger LOGGER = LogManager.getLogger(NavigateActions.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private ProxyMonitor proxyMonitor;

    private WebDriver driver;

    @Override
    public void navigateWithoutValidations(String page)
    {
        LOGGER.debug("Trying to load the page: {}", page);
        getDriver().navigate().to(page);
        LOGGER.debug("Page {} loaded", page);
    }

    @Override
    public WebDriver get(URL page)
    {
        try
        {
            LOGGER.debug("Trying to load the page: {}", page);
            WebDriver driver = getDriver();
            driver.navigate().to(page);
            if (proxyConnectionAlive(driver) && !proxyMonitor.ifVerificationAppered(driver))
            {
                LOGGER.debug("Page {} loaded", page);
                return driver;
            }
            else
            {
                LOGGER.debug("Either proxyConnection is lost OR validation page is opened");
                return restartDriver();
            }
        }
        catch (TimeoutException e)
        {
            LOGGER.error("Error while loading the page: {}, {}", page, e);
            return restartDriver();
        }
    }

    private boolean proxyConnectionAlive(WebDriver driver)
    {
        return driver.findElements(By.xpath("//div[@class='error-code']")).isEmpty();
    }

    private WebDriver restartDriver()
    {
        LOGGER.info("Restarting driver ...");
        webDriverProvider.end();
        return webDriverProvider.get();
    }

    private WebDriver getDriver()
    {
        return webDriverProvider.get();
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }
}
