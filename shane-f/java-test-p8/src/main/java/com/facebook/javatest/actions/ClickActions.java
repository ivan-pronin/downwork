package com.facebook.javatest.actions;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

public class ClickActions
{
    private static final Logger LOGGER = LogManager.getLogger(ClickActions.class);

    private WebDriver driver;

    public void click(List<WebElement> elements)
    {
        if (elements.isEmpty())
        {
            LOGGER.debug("No elements to click...");
            return;
        }
        WebElement element = elements.get(0);
        click(element);
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
            scrollToElement(element);
            scrollScreenByPixels(-200);
            element.click();
        }
        LOGGER.debug("Element was clicked: {}", element);
    }

    public void scrollToTheBottom()
    {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void setWebDriver(WebDriver driver)
    {
        this.driver = driver;
    }

    private void scrollScreenByPixels(int pixels)
    {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        String script = "window.scrollBy(0, %s)";
        js.executeScript(String.format(script, pixels));
    }

    private void scrollToElement(WebElement element)
    {
        JavascriptExecutor js = ((JavascriptExecutor) driver);
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }
}
