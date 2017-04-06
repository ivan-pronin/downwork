package com.idealista.scraper.ui;

import java.util.Collections;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

@Component
public class SearchActions
{
    private static final Logger LOGGER = LogManager.getLogger(SearchActions.class);

    private WebDriver driver;

    public List<WebElement> findElementsById(List<WebElement> rootElement, String id)
    {
        if (!rootElement.isEmpty())
        {
            return rootElement.get(0).findElements(By.id(id));
        }
        return driver.findElements(By.id(id));
    }

    public List<WebElement> findElementsById(String id)
    {
        return findElementsById(Collections.emptyList(), id);
    }

    public List<WebElement> findElementsByTagName(List<WebElement> rootElement, String tagName)
    {
        if (!rootElement.isEmpty())
        {
            return rootElement.get(0).findElements(By.tagName(tagName));
        }
        return Collections.emptyList();
    }

    public List<WebElement> findElementsByXpath(List<WebElement> rootElement, String xpath)
    {
        if (!rootElement.isEmpty())
        {
            return findElementsByXpath(rootElement.get(0), xpath);
        }
        return Collections.emptyList();
    }

    public List<WebElement> findElementsByXpath(String xpath)
    {
        return driver.findElements(By.xpath(xpath));
    }

    public List<WebElement> findElementsByXpath(WebElement rootElement, String xpath)
    {
        if (rootElement != null)
        {
            return rootElement.findElements(By.xpath("." + xpath));
        }
        return Collections.emptyList();
    }
    
    public String getAttribute(WebElement element, String attributeName)
    {
        if (element == null || attributeName == null)
        {
            LOGGER.warn("GetAttribute: element or attribute is null, returning null");
            return null;
        }
        return element.getAttribute(attributeName);
    }

    public String getElementText(List<WebElement> rootElement)
    {
        if (!rootElement.isEmpty())
        {
            return rootElement.get(0).getText();
        }
        return null;
    }

    public String getElementTextByXpath(String xpath)
    {
        return getElementText(findElementsByXpath(xpath));
    }

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
    
    public boolean waitForElementsDisappear(List<WebElement> elements, int seconds)
    {
        try
        {
            LOGGER.debug("Waiting for ALL elements to disappear: {}", elements);
            Boolean disappeared = (new WebDriverWait(driver, seconds))
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
