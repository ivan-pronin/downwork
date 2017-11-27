package com.idealista.scraper.ui.actions.base;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import com.idealista.scraper.util.WaitUtils;

public abstract class BaseClickActions
{
    private static final Logger LOGGER = LogManager.getLogger(BaseClickActions.class);

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
            // WebDriverUtils.takeScreenShot("Right after error", driver);
            JavascriptExecutor js = ((JavascriptExecutor) getWebDriver());
            js.executeScript("arguments[0].scrollIntoView();", element);
            // WebDriverUtils.takeScreenShot("After scrollIntoView() attempt", driver);
            element.click();
        }
        LOGGER.debug("Element was clicked: {}", element);
    }

    public void scrollScreenDown()
    {
        JavascriptExecutor js = ((JavascriptExecutor) getWebDriver());
        js.executeScript("window.scrollBy(0, 1000)");
    }

    public void scrollScreenDownByPixels(int pixels)
    {
        JavascriptExecutor js = ((JavascriptExecutor) getWebDriver());
        String script = "window.scrollBy(0, %s)";
        js.executeScript(String.format(script, pixels));
    }

    public void scrollToElement(WebElement element)
    {
        JavascriptExecutor js = ((JavascriptExecutor) getWebDriver());
        js.executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public void scrollToElementAndWait(List<WebElement> elements, int elementIndex)
    {
        scrollToElement(elements.get(elementIndex));
        WaitUtils.sleep(this, 500);
    }

    public void scrollToTheBottom()
    {
        JavascriptExecutor js = ((JavascriptExecutor) getWebDriver());
        js.executeScript("window.scrollTo(0, document.body.scrollHeight)");
    }

    public void scrollToTheTop()
    {
        JavascriptExecutor js = ((JavascriptExecutor) getWebDriver());
        js.executeScript("window.scrollTo(0, 0)");
    }

    public void setElementTextFast(WebElement element, String text)
    {
        if (element == null)
        {
            LOGGER.debug("Element to enter text is null ...");
            return;
        }
        element.clear();
        element.sendKeys(text);
        System.out.println("");
        LOGGER.info("Text <{}> was entered to element <{}>", text, element);
    }

    protected abstract WebDriver getWebDriver();
}
