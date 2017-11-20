package com.idealista.scraper.ui.actions;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.scraper.webdriver.WebDriverProvider;

@Component
public class WaitActions
{
    private static final Logger LOGGER = LogManager.getLogger(SearchActions.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    private WebDriver webDriver;

    public WebElement waitForElement(By locator, int seconds)
    {
        try
        {
            LOGGER.debug("Waiting for visibility of an element: {}", locator);
            WebElement element = (new WebDriverWait(getWebDriver(), seconds))
                    .until(ExpectedConditions.visibilityOfElementLocated(locator));
            LOGGER.debug("Element appeared!");
            return element;
        }
        catch (TimeoutException e)
        {
            LOGGER.warn("Failed to wait for visibility of an element: {}", locator);
            return null;
        }
    }

    public boolean waitForElementDisappear(By locator, int seconds)
    {
        try
        {
            LOGGER.debug("Waiting for an element to disappear: {}", locator);
            Boolean disappeared = (new WebDriverWait(getWebDriver(), seconds))
                    .until(ExpectedConditions.invisibilityOfElementLocated(locator));
            LOGGER.debug("Element disappeared!");
            return disappeared;
        }
        catch (TimeoutException e)
        {
            LOGGER.debug("Failed for an element to disappear: {}, {}", locator, e.getMessage());
            return false;
        }
    }

    public List<WebElement> waitForElements(By locator, int seconds)
    {
        try
        {
            LOGGER.debug("Waiting for presence of elements: {}", locator);
            List<WebElement> elements = (new WebDriverWait(getWebDriver(), seconds))
                    .until(ExpectedConditions.presenceOfAllElementsLocatedBy(locator));
            LOGGER.debug("Elements appeared!");
            return elements;
        }
        catch (TimeoutException e)
        {
            LOGGER.warn("Failed to wait for presence of elements: {}", locator);
            return null;
        }
    }

    public boolean waitForElementsDisappear(List<WebElement> elements, int seconds)
    {
        try
        {
            LOGGER.debug("Waiting for ALL elements to disappear: {}", elements);
            Boolean disappeared = (new WebDriverWait(getWebDriver(), seconds))
                    .until(ExpectedConditions.invisibilityOfAllElements(elements));
            LOGGER.debug("Element disappeared!");
            return disappeared;
        }
        catch (TimeoutException e)
        {
            LOGGER.debug("Failed for ALL elements to disappear: {}, {}", elements, e.getMessage());
            return false;
        }
    }

    public List<WebElement> waitForElementsToRefresh(By locator, int seconds)
    {
        try
        {
            LOGGER.debug("Waiting for an element to refresh: {}", locator);
            List<WebElement> elements = (new WebDriverWait(getWebDriver(), seconds))
                    .until(ExpectedConditions.refreshed(ExpectedConditions.presenceOfAllElementsLocatedBy(locator)));
            LOGGER.debug("Element appeared!");
            return elements;
        }
        catch (TimeoutException e)
        {
            LOGGER.warn("Failed to wait for an element to refresh: {}", locator);
            return null;
        }
    }

    public boolean waitForElementsToStale(WebElement element, int seconds)
    {
        try
        {
            LOGGER.debug("Waiting for an element to stale: {}", element);
            boolean stale = (new WebDriverWait(getWebDriver(), seconds)).until(ExpectedConditions.stalenessOf(element));
            LOGGER.debug("Element appeared!");
            return stale;
        }
        catch (TimeoutException e)
        {
            LOGGER.warn("Failed to wait for an element to stale: {}", element);
            return false;
        }
    }

    private WebDriver getWebDriver()
    {
        if (webDriverProvider.isWebDriverInitialized())
        {
            return webDriverProvider.get();
        }
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver)
    {
        this.webDriver = webDriver;
    }
}
