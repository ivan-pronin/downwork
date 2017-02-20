package com.idealista.scraper.page;

import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.util.Set;

public class StartPageTests
{

    private static final Logger LOGGER = LogManager.getLogger(StartPageTests.class);
    private WebDriverProvider webDriverProvider = new WebDriverProvider();

    // @Test
    public void testSelectAction() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to("https://www.idealista.com/en/");
        StartPage startPage = new StartPage(driver);
        startPage.selectOperation("Buy");
        synchronized (this)
        {
            this.wait(1000);
        }
        startPage.selectOperation("Share");
        synchronized (this)
        {
            this.wait(1000);
        }
        startPage.selectOperation("Rent");
    }

    // @Test
    public void testGetAvailableTypologies()
    {
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to("https://www.idealista.com/en/");
        StartPage startPage = new StartPage(driver);
        startPage.selectOperation("Buy");
        LOGGER.info(startPage.getAvailableTypologies());
        startPage.selectOperation("Share");
        LOGGER.info(startPage.getAvailableTypologies());
        startPage.selectOperation("Rent");
        LOGGER.info(startPage.getAvailableTypologies());
    }

    @Test
    public void testGetAvailableLocations()
    {
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to("https://www.idealista.com/en/");
        StartPage startPage = new StartPage(driver);
        startPage.selectOperation("Buy");
        startPage.selectTypology("Homes");
        Set<String> availableLocations = startPage.getAvailableLocations();
        LOGGER.info("Total available locations: {}. List: {}", availableLocations.size(),
                availableLocations);
    }

    //@Test
    public void testSelectLocationAndSearch()
    {
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to("https://www.idealista.com/en/");
        StartPage startPage = new StartPage(driver);
        startPage.selectOperation("Buy");
        startPage.selectTypology("Homes");
        startPage.selectLocation("Barcelona");
        startPage.clickSearch();
    }

}
