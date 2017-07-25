package com.facebook.javatest.actions;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

public class ElementActions
{
    private static final String COULD_NOT_FIND_ELEMENT = "Could not find element ";
    private static final Logger LOGGER = LogManager.getLogger(ElementActions.class);
    
    public ElementActions()
    {
    }

    public void enterTextToElement(String text, List<WebElement> element)
    {
        if (!element.isEmpty())
        {
            WebElement firstElement = element.get(0);
            firstElement.clear();
            firstElement.sendKeys(text);
            LOGGER.info("Entering text {} to element {}", text, firstElement);
            return;
        }
        LOGGER.error(COULD_NOT_FIND_ELEMENT + "to enter text: " + text);
    }

    public String getAttribute(List<WebElement> element, String attributeName)
    {
        if (!element.isEmpty())
        {
            return element.get(0).getAttribute(attributeName);
        }
        LOGGER.warn("Could not get attribute {} for element {}", attributeName, element);
        return null;
    }
}
