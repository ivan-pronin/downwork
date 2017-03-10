package com.idealista.scraper.ui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SearchActions
{
    private static final Logger LOGGER = LogManager.getLogger(SearchActions.class);

    private WebDriver driver;

    public WebElement waitForElement(By locator, int seconds)
    {
        try
        {
            LOGGER.debug("Waiting for an element: {}", locator);
            WebElement element = (new WebDriverWait(driver, seconds))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            LOGGER.debug("Element appeared!");
            return element;
        }
        catch (TimeoutException e)
        {
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

    public List<WebElement> findElementsByXpath(String xpath)
    {
        return driver.findElements(By.xpath(xpath));
    }

    public List<WebElement> findElementsByXpath(List<WebElement> rootElement, String xpath)
    {
        if (!rootElement.isEmpty())
        {
            return rootElement.get(0).findElements(By.xpath("." + xpath));
        }
        return Collections.emptyList();
    }

    public List<WebElement> findElementsByTagName(List<WebElement> rootElement, String tagName)
    {
        if (!rootElement.isEmpty())
        {
            return rootElement.get(0).findElements(By.tagName(tagName));
        }
        return Collections.emptyList();
    }

    public List<WebElement> findElementsById(List<WebElement> rootElement, String id)
    {
        if (!rootElement.isEmpty())
        {
            return rootElement.get(0).findElements(By.id(id));
        }
        return driver.findElements(By.id(id));
    }

    public String getElementText(List<WebElement> rootElement)
    {
        if (!rootElement.isEmpty())
        {
            return rootElement.get(0).getText();
        }
        return null;
    }

    public void setWebDriver(WebDriver driver)
    {
        this.driver = driver;
    }
}
