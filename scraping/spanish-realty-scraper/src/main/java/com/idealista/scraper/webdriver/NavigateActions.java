package com.idealista.scraper.webdriver;

import java.net.MalformedURLException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

@Component
public class NavigateActions implements INavigateActions
{
    private static final Logger LOGGER = LogManager.getLogger(NavigateActions.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private ProxyMonitor proxyMonitor;

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

    @Override
    public void navigateWithoutValidations(String page)
    {
        LOGGER.debug("Trying to load the page: {}", page);
        getDriver().navigate().to(page);
        LOGGER.debug("Page {} loaded", page);
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }

    private WebDriver getDriver()
    {
        return webDriverProvider.get();
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

    @Override
    public WebDriver get(String page)
    {
        try
        {
            return get(new URL(page));
        }
        catch (MalformedURLException e)
        {
            LOGGER.error("Failed to navigate to URL: {} with error: {}", page, e.getMessage());
        }
        return null;
    }
}
