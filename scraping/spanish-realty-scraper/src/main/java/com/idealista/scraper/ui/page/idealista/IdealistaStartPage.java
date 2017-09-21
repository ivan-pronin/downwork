package com.idealista.scraper.ui.page.idealista;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.idealista.scraper.ui.page.BasePage;
import com.idealista.scraper.ui.page.IStartPage;

@Component
public class IdealistaStartPage extends BasePage implements IStartPage
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaStartPage.class);

    public void clickSearch()
    {
        List<WebElement> link = searchActions.findElementsByXpath("//button[@class='btn action']");
        if (!link.isEmpty())
        {
            link.get(0).click();
            return;
        }
        LOGGER.warn("Failed to find <search> button");
    }

    public Set<String> getAvailableLocations()
    {
        List<WebElement> operationCombo = searchActions.findElementsById("location-combo");
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

    public Set<String> getAvailableOperations()
    {
        WebElement operationCombo = waitActions.waitForElement(By.id("operation-combo"), 10);
        if (operationCombo != null)
        {
            List<WebElement> options = operationCombo.findElements(By.xpath(".//li[@data-disabled='false']"));
            return options.stream().map(WebElement::getText).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public Set<String> getAvailableTypologies()
    {
        WebElement operationCombo = waitActions.waitForElement(By.id("typology-combo"), 10);
        if (operationCombo != null)
        {
            List<WebElement> options = operationCombo.findElements(By.xpath(".//li[@data-disabled='false']"));
            return options.stream().map(WebElement::getText).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public void selectLocation(String location)
    {
        List<WebElement> operationCombo = searchActions.findElementsById("location-combo");
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

    public void selectOperation(String operation)
    {
        if (operation == null)
        {
            LOGGER.info("Specified <operation> is null, skipping...");
            return;
        }
        WebElement operationCombo = waitActions.waitForElement(By.id("operation-combo"), 10);
        if (operationCombo != null)
        {
            List<WebElement> options = operationCombo.findElements(By.tagName("li"));
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

    public void selectTypology(String typology)
    {
        if (typology == null)
        {
            LOGGER.info("Specified <typology> is null, skipping...");
            return;
        }
        List<WebElement> operationCombo = searchActions.findElementsById("typology-combo");
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
}
