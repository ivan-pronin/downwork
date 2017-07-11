package com.idealista.scraper.ui.page.pisos;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.idealista.scraper.ui.page.BasePage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Component
public class PisosStartPage extends BasePage
{
    private static final String OPERATION_ID = "dd_tipoOperacion";
    private static final String TYPOLOGY_ID = "dd_tipoInmueble";
    private static final String LOCATION_ID = "listazonas";
    private static final Logger LOGGER = LogManager.getLogger(PisosStartPage.class);

    public void clickSearch()
    {
        List<WebElement> link = searchActions.findElementsById("btnBuscarHome");
        clickActions.click(link);
    }

    public Set<String> getAvailableLocations()
    {
        List<WebElement> operationCombo = searchActions.findElementsById(LOCATION_ID);
        if (!operationCombo.isEmpty())
        {
            List<WebElement> availableOptions = operationCombo.get(0).findElements(By.tagName("li"));
            Set<String> results = availableOptions.stream().map(WebElement::getText).collect(Collectors.toSet());
            return results;
        }
        return Collections.emptySet();
    }

    public Set<String> getAvailableOperations()
    {
        WebElement operationCombo = waitActions.waitForElement(By.id(OPERATION_ID), 10);
        if (operationCombo != null)
        {
            List<WebElement> options = operationCombo.findElements(By.tagName("li"));
            return options.stream().map(WebElement::getText).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public Set<String> getAvailableTypologies()
    {
        WebElement operationCombo = waitActions.waitForElement(By.id(TYPOLOGY_ID), 10);
        if (operationCombo != null)
        {
            WebElement element = operationCombo.findElement(By.tagName("li"));
            waitActions.waitForElementsToStale(element, 3);
            List<WebElement> options = waitActions
                    .waitForElementsToRefresh(By.xpath("//*[@id='" + TYPOLOGY_ID + "']//li"), 5);
            return options.stream().map(WebElement::getText).collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    public void selectLocation(String location)
    {
        List<WebElement> operationCombo = searchActions.findElementsById(LOCATION_ID);
        if (!operationCombo.isEmpty())
        {
            List<WebElement> options = operationCombo.get(0).findElements(By.tagName("li"));
            for (WebElement option : options)
            {
                if (location.equalsIgnoreCase(option.getText()))
                {
                    option.click();
                    LOGGER.info("Clicked on <location> option with text: {}", location);
                    return;
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
        WebElement operationCombo = waitActions.waitForElement(By.id(OPERATION_ID), 10);
        if (operationCombo != null)
        {
            List<WebElement> options = operationCombo.findElements(By.tagName("li"));
            for (WebElement option : options)
            {
                if (operation.equalsIgnoreCase(option.getText()))
                {
                    option.click();
                    LOGGER.info("Clicked on <operation> option with text: {}", operation);
                    return;
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
        waitActions.waitForElementsToStale(searchActions.findElementsById(TYPOLOGY_ID).get(0), 3);
        List<WebElement> operationCombo = searchActions.findElementsById(TYPOLOGY_ID);
        if (!operationCombo.isEmpty())
        {
            List<WebElement> options = operationCombo.get(0).findElements(By.tagName("li"));
            for (WebElement option : options)
            {
                if (typology.equalsIgnoreCase(option.getText()))
                {
                    option.click();
                    LOGGER.info("Clicked on <topology> option with text: {}", typology);
                    return;
                }
            }
        }
    }
}
