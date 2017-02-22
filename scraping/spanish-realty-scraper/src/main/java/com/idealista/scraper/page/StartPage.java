package com.idealista.scraper.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class StartPage
{
    private static final Logger LOGGER = LogManager.getLogger(StartPage.class);

    private WebDriver driver;

    public StartPage(WebDriver driver)
    {
        this.driver = driver;
    }

    public void selectOperation(String operation)
    {
        if (operation == null)
        {
            LOGGER.info("Specified <operation> is null, skipping...");
            return;
        }
        List<WebElement> operationCombo = driver.findElements(By.id("operation-combo"));
        if (!operationCombo.isEmpty())
        {
            List<WebElement> options = operationCombo.get(0).findElements(By.tagName("li"));
            for (WebElement option : options)
            {
                if (operation.equalsIgnoreCase(option.getText()))
                {
                    option.click();
                    LOGGER.info("Clicked on <operation> option with text: {}", operation);
                }
            }
        }
    }

    public Set<String> getAvailableOperations()
    {
        List<WebElement> operationCombo = driver.findElements(By.id("operation-combo"));
        if (!operationCombo.isEmpty())
        {
            List<WebElement> options = operationCombo.get(0).findElements(By.xpath(".//li[@data-disabled='false']"));
            return options.stream().map(WebElement::getText).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public Set<String> getAvailableTypologies()
    {
        List<WebElement> operationCombo = driver.findElements(By.id("typology-combo"));
        if (!operationCombo.isEmpty())
        {
            List<WebElement> options = operationCombo.get(0).findElements(By.xpath(".//li[@data-disabled='false']"));
            return options.stream().map(WebElement::getText).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public void selectTypology(String typology)
    {
        if (typology == null)
        {
            LOGGER.info("Specified <typology> is null, skipping...");
            return;
        }
        List<WebElement> operationCombo = driver.findElements(By.id("typology-combo"));
        if (!operationCombo.isEmpty())
        {
            List<WebElement> options = operationCombo.get(0).findElements(By.xpath(".//li[@data-disabled='false']"));
            for (WebElement option : options)
            {
                if (typology.equalsIgnoreCase(option.getText()))
                {
                    option.click();
                    LOGGER.info("Clicked on <topology> option with text: {}", typology);
                }
            }
        }
    }

    public Set<String> getAvailableLocations()
    {
        List<WebElement> operationCombo = driver.findElements(By.id("location-combo"));
        if (!operationCombo.isEmpty())
        {
            List<WebElement> availableOptions = operationCombo.get(0)
                    .findElements(By.xpath(".//li[@data-disabled='false']"));
            Set<String> results = availableOptions.stream().map(WebElement::getText).collect(Collectors.toSet());
            results.remove("International");
            return results;
        }
        return Collections.emptySet();
    }

    public void selectLocation(String location)
    {
        List<WebElement> operationCombo = driver.findElements(By.id("location-combo"));
        if (!operationCombo.isEmpty())
        {
            List<WebElement> options = operationCombo.get(0).findElements(By.xpath(".//li[@data-disabled='false']"));
            for (WebElement option : options)
            {
                if (location.equalsIgnoreCase(option.getText()))
                {
                    option.click();
                    LOGGER.info("Clicked on <location> option with text: {}", location);
                }
            }
        }
    }

    public void clickSearch()
    {
        List<WebElement> link = driver.findElements(By.xpath("//button[@class='btn action']"));
        if (!link.isEmpty())
        {
            link.get(0).click();
            return;
        }
        LOGGER.warn("Failed to find <search> button");
    }
}
