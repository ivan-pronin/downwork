package com.crunchbase.scraper.ui;

import com.crunchbase.scraper.service.CrunchbaseScraperService;
import com.crunchbase.scraper.util.WebDriverUtils;
import com.crunchbase.scraper.webdriver.INavigateActions;
import com.crunchbase.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class PageLoader
{
    private static final int WAIT_FOR_ELEMENT_SECONDS = 10;
    private static final Logger LOGGER = LogManager.getLogger(PageLoader.class);
    private static int PAGE_LOAD_ATTEMPTS = 3;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private WebDriverProvider webDriverProvider;

    public boolean tryToLoadStartPage()
    {
        int counter = 0;
        while (counter < PAGE_LOAD_ATTEMPTS)
        {
            counter++;
            WebDriver driver = navigateActions.get(CrunchbaseScraperService.CRUNCHBASE_COM);
            WebDriverUtils.waitForJSToLoad(driver);
            SearchActions searchActions = new SearchActions();
            searchActions.setWebDriver(driver);

            if (!h2WithTextLoaded(searchActions, "Featured Lists and Searches"))
            {
                continue;
            }
            if (!h2WithTextLoaded(searchActions, "Trending on Crunchbase"))
            {
                continue;
            }
            LOGGER.info("MainPage loaded at counter: {}", counter);
            return true;
        }
        LOGGER.warn("Failed to load Main page at counter: {}", counter);
        return false;
    }

    private boolean h2WithTextLoaded(SearchActions searchActions, String h2Text)
    {
        return searchActions.waitForElement(By.xpath(String.format("//h2[text()='%s']", h2Text)),
                WAIT_FOR_ELEMENT_SECONDS) != null;
    }

    public boolean tryToLoadSearchResultsPage()
    {
        int counter = 0;
        while (counter < PAGE_LOAD_ATTEMPTS)
        {
            counter++;
            WebDriver driver = webDriverProvider.get();
            SearchActions searchActions = new SearchActions();
            searchActions.setWebDriver(driver);
            searchActions.waitForElementDisappear(By.xpath("//h2[text()='Companies']"), 5);
            WebDriverUtils.waitForPageLoad(driver);
            WebDriverUtils.waitForJSToLoad(driver);
            WebElement resultsInfo = searchActions.waitForElement(By.xpath("//results-info/h3[contains(.,'result')]"),
                    20);
            if (resultsInfo == null)
            {
                continue;
            }
            else
            {
                LOGGER.info("SearchResults page loaded at counter: {}", counter);
                return true;
            }
        }
        LOGGER.warn("Failed to load SearchResults page at counter: {}", counter);
        return false;
    }
}
