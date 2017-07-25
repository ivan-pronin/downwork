package com.facebook.javatest.actions;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WaitActions
{
    private static final Logger LOGGER = LogManager.getLogger(SearchActions.class);

    private WebDriver driver;
    
    public void setWebDriver(WebDriver driver)
    {
        this.driver = driver;
    }
    
    public WebElement waitForElement(By locator, int seconds)
    {
        try
        {
            LOGGER.debug("Waiting for visibility of an element: {}", locator);
            WebElement element = (new WebDriverWait(driver, seconds))
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
            Boolean disappeared = (new WebDriverWait(driver, seconds))
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
            List<WebElement> elements = (new WebDriverWait(driver, seconds))
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
}
