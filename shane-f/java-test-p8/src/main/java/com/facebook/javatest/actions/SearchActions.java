package com.facebook.javatest.actions;

import java.util.Collections;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class SearchActions
{
    private WebDriver driver;

    public List<WebElement> findElementsById(String id)
    {
        return findElementsById(Collections.emptyList(), id);
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

    private List<WebElement> findElementsById(List<WebElement> rootElement, String id)
    {
        if (!rootElement.isEmpty())
        {
            return rootElement.get(0).findElements(By.id(id));
        }
        return driver.findElements(By.id(id));
    }
}
