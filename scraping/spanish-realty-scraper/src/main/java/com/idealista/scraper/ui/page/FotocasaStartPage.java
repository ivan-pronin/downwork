package com.idealista.scraper.ui.page;

import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.util.StringUtils;

public class FotocasaStartPage extends BasePage
{
    private static final By SEARCH_BUTTON_LOCATOR = By.xpath("//button[@class='re-Search-submit--button']");
    private static final String LABEL_FOR_LOCATOR_PATTERN = "//label[@for='%s']";
    private static final Logger LOGGER = LogManager.getLogger(FotocasaStartPage.class);

    public void selectOptionsAndStartSearch(String operation, String searchString, boolean newHomes)
    {
        LOGGER.info("Starting search with options: operation={}, searchString={}, newHomes={} ", operation,
                searchString, newHomes);
        selectOperation(operation);
        selectNewHomes(newHomes);
        if (!enterSearchString(searchString))
        {
            clickStartSearch();
        }
    }

    private void clickStartSearch()
    {
        WebElement button = searchActions.waitForElement(SEARCH_BUTTON_LOCATOR, 5);
        clickActions.click(button);
    }

    private boolean enterSearchString(String searchString)
    {
        if (StringUtils.isEmpty(searchString))
        {
            LOGGER.info("SearchString is empty, skipping this field");
            return false;
        }
        WebElement searchField = searchActions.waitForElement(By.xpath("//input[@class='sui-Autocompleted-input']"), 5);
        clickActions.setElementTextFast(searchField, searchString);
        WebElement autocomplete = searchActions.waitForElement(By.xpath("//ul[@class='sui-Autocompleted-results']"), 5);
        if (autocomplete != null)
        {
            List<WebElement> items = searchActions.findElementsByXpath(autocomplete, "//li");
            if (!items.isEmpty())
            {
                for (WebElement item : items)
                {
                    String text = item.getText();
                    if (searchString.equalsIgnoreCase(text))
                    {
                        clickActions.click(item);
                        searchActions.waitForElementDisappear(SEARCH_BUTTON_LOCATOR, 10);
                        return true;
                    }
                }
                LOGGER.warn("Exact autocomplete match wasn't found, clicking the first link");
                clickActions.click(items);
                return true;
            }
        }
        return false;
        
    }

    private String parseIdFromOperation(String operation)
    {
        String id = null;
        switch (operation)
        {
            case "Venta":
                id = "transactionTypeId-1";
                break;
            case "Alquiler":
                id = "transactionTypeId-3";
                break;
            case "Compartir":
                id = "transactionTypeId-5";
                break;
            case "Vacacional":
                id = "transactionTypeId-8";
                break;
            default:
                throw new IllegalArgumentException("Invalid <operation> value provided: " + operation);
        }
        return String.format(LABEL_FOR_LOCATOR_PATTERN, id);
    }

    private void selectNewHomes(boolean newHomes)
    {
        List<WebElement> checkBox = searchActions.findElementsByXpath(getContainerApp(),
                String.format(LABEL_FOR_LOCATOR_PATTERN, "onCheckboxField"));
        if (checkBox.isEmpty())
        {
            LOGGER.error("Checkbox NewHomes was not found");
            return;
        }
        WebElement checkboxElement = checkBox.get(0);
        boolean enabled = checkboxElement.isEnabled();
        boolean selected = checkboxElement.isSelected();
        LOGGER.info("Current state of NewHomes: {}", selected);
        if (newHomes != selected && enabled)
        {
            checkboxElement.click();
            LOGGER.info("NewHomes was clicked, current state is: {}", checkboxElement.isEnabled());
            return;
        }
        LOGGER.info("NewHomes checkbox wasn't changed");
    }

    private void selectOperation(String operation)
    {
        List<WebElement> elements = searchActions.findElementsByXpath(getContainerApp(),
                parseIdFromOperation(operation));
        clickActions.click(elements);
    }

    private List<WebElement> getContainerApp()
    {
        return searchActions.waitForElements(By.id("App"), 5);
    }
}
