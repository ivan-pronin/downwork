package com.cactusglobal.whiteboard.action;

import com.cactusglobal.whiteboard.Application;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class ElementActions
{
    private static final Logger LOGGER = LogManager.getLogger(Application.class);
    private static final String COULD_NOT_FIND_ELEMENT = "Could not find element ";
    
    private WebDriver driver;
    private int waitForElementTimeoutSeconds;

    public ElementActions(WebDriver driver, int waitForElementTimeout)
    {
        this.driver = driver;
        this.waitForElementTimeoutSeconds = waitForElementTimeout;
    }

    public WebElement waitForElement(String elementId)
    {
        return waitForElement(elementId, waitForElementTimeoutSeconds);
    }

    public WebElement waitForElement(String elementId, int seconds)
    {
        WebElement emailInput = (new WebDriverWait(driver, seconds))
                .until(ExpectedConditions.presenceOfElementLocated(By.id(elementId)));
        return emailInput;
    }

    public WebElement waitForElement(By locator, int seconds)
    {
        try
        {
            WebElement element = (new WebDriverWait(driver, seconds))
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
            return element;
        }
        catch (TimeoutException e)
        {
            return null;
        }
    }

    public void enterTextById(String id, String text)
    {
        WebElement element = waitForElement(id);
        enterTextToElement(text, element);
    }

    public void click(By locator)
    {
        WebElement element = driver.findElement(locator);
        if (element != null)
        {
            element.click();
            LOGGER.info("Clicked on element with locator: {}", locator);
            return;
        }
        LOGGER.error(COULD_NOT_FIND_ELEMENT + "to click with locator: {}", locator);
    }

    public void enterTextToElement(String text, WebElement element)
    {
        if (element != null)
        {
            element.clear();
            element.sendKeys(text);
            LOGGER.info("Entering text: {}", text);
            return;
        }
        LOGGER.error(COULD_NOT_FIND_ELEMENT + "to enter text: {}", text);
    }
}
