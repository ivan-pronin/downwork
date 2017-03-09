package com.idealista.scraper.webdriver;

import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public class NavigateActions implements INavigateActions
{
    private static final Logger LOGGER = LogManager.getLogger(NavigateActions.class);

    private WebDriverProvider webDriverProvider;
    private ProxyMonitor proxyMonitor = new ProxyMonitor();
    private WebDriver driver;

    public NavigateActions(WebDriver driver)
    {
        this.driver = driver;
    }

    public NavigateActions(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
        driver = webDriverProvider.get();
    }

    @Override
    public void navigateWithoutValidations(String page)
    {
        LOGGER.debug("Trying to load the page: {}", page);
        driver.navigate().to(page);
        LOGGER.debug("Page {} loaded", page);
    }

    @Override
    public WebDriver get(URL page)
    {
        try
        {
            LOGGER.debug("Trying to load the page: {}", page);
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
}
