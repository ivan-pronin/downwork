package com.crunchbase.scraper.ui;

import com.crunchbase.scraper.util.WaitUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ClickActions
{
    private static final Logger LOGGER = LogManager.getLogger(ClickActions.class);

    private WebDriver driver;

    public void setElementTextFast(List<WebElement> elements, String text)
    {
        if (elements.isEmpty())
        {
            errorNoElements();
            return;
        }
        setElementText(elements, text);
    }

    private void setElementText(List<WebElement> elements, String text)
    {
        WebElement webElement = elements.get(0);
        webElement.clear();
        webElement.sendKeys(text);
        LOGGER.info("Text <{}> was entered fast to element <{}>", text, webElement);
    }

    private void errorNoElements()
    {
        LOGGER.debug("No elements to enter text...");
    }

    public void setLastElementTextSlowly(List<WebElement> elements, String text)
    {
        if (elements.isEmpty())
        {
            errorNoElements();
            return;
        }
        WebElement webElement = elements.get(0);
        webElement.clear();
        char[] chars = text.toCharArray();
        int length = text.length();
        for (int i = 0; i < length; i++)
        {
            if (i +2 >= length)
            {
                WaitUtils.sleep(this, 3000);
            }
            webElement.sendKeys("" + chars[i]);
        }
        LOGGER.info("Text <{}> was entered to element <{}>", text, webElement);
    }
    
    public void setElementTextSlowly(List<WebElement> elements, String text)
    {
        if (elements.isEmpty())
        {
            errorNoElements();
            return;
        }
        WebElement webElement = elements.get(0);
        webElement.clear();
        for (char c : text.toCharArray())
        {
            webElement.sendKeys("" + c);
            WaitUtils.sleep(this, 500);
        }
        LOGGER.info("Text <{}> was entered to element <{}>", text, webElement);
    }

    public void setElementTextFastAndWait(List<WebElement> elements, String text)
    {
        if (elements.isEmpty())
        {
            errorNoElements();
            return;
        }
        setElementText(elements, text);
        WaitUtils.sleepSeconds(this, 5);
    }

    public void clickViaJs(List<WebElement> elements)
    {
        if (elements.isEmpty())
        {
            LOGGER.debug("No elements to click...");
            return;
        }
        WebElement element = elements.get(0);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click;", element);
        LOGGER.debug("Element was clicked via JS: {}", element);
    }
    
    public void click(List<WebElement> elements)
    {
        if (elements.isEmpty())
        {
            LOGGER.debug("No elements to click...");
            return;
        }
        WebElement element = elements.get(0);
        click(element);
        LOGGER.debug("Element was clicked: {}", element);
    }

    public void click(WebElement element)
    {
        if (element == null)
        {
            LOGGER.debug("Element to click is null...");
            return;
        }
        LOGGER.debug("Clicking the first element with xpath: {}", element);
        try
        {
            element.click();
        }
        catch (WebDriverException e)
        {
            // scrolling for element into view
            LOGGER.debug("Could not click the element: {}", e.getMessage());
            JavascriptExecutor js = ((JavascriptExecutor) driver);
            js.executeScript("arguments[0].scrollIntoView();", element);
            element.click();
        }
        LOGGER.debug("Element was clicked: {}", element);
    }

    public void setWebDriver(WebDriver driver)
    {
        this.driver = driver;
    }
}
