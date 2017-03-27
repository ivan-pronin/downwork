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
        WebElement webElement = elements.get(0);
        webElement.clear();
        webElement.sendKeys(text);
    }

    private void errorNoElements()
    {
        LOGGER.debug("No elements to enter text...");
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

    public void click(List<WebElement> elements)
    {
        if (elements.isEmpty())
        {
            LOGGER.debug("No elements to click...");
            return;
        }
        click(elements.get(0));
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
