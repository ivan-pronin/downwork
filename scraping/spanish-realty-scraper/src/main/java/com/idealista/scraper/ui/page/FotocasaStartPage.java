package com.idealista.scraper.ui.page;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

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
        WebElement clearTextField = searchActions.waitForElement(By.xpath("//span[@class='sui-Autocompleted-clear']"), 2);
        clickActions.click(clearTextField);
        
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
                        searchActions.waitForElementsDisappear(items, 10);
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

    public void selectSubDistrict(String subDistrict)
    {
        LOGGER.info("Selecting subDistrict: {}", subDistrict);
        List<WebElement> subDisctricts = searchActions
                .waitForElements(By.xpath("(//span[@class='re-GeographicSearch-listTriggerText'])[5]"), 10);
        clickActions.click(subDisctricts);
        List<WebElement> disctrictsDropDown = waitForSubDistrictList();
        unselectSubDistrics(disctrictsDropDown);
        disctrictsDropDown = waitForSubDistrictList();
        selectItemFromList(disctrictsDropDown, subDistrict);
        clickApplySubDistrics(disctrictsDropDown);
        LOGGER.info("SubDistrict: {} has been selected", subDistrict);
    }

    public void selectDistrict(String district)
    {
        LOGGER.info("Selecting district: {}", district);
        List<WebElement> disctricts = searchActions
                .waitForElements(By.xpath("(//span[@class='re-GeographicSearch-listTriggerText'])[4]"), 10);
        String text = searchActions.getElementText(disctricts);
        if (text != null && !text.equalsIgnoreCase(district))
        {
            clickActions.click(disctricts);
            List<WebElement> disctrictsDropDown = waitForDistrictList();
            unselectSubDistrics(disctrictsDropDown);
            selectItemFromList(disctrictsDropDown, district);
            clickApplyDistrics(disctrictsDropDown);
            LOGGER.info("District: {} was selected", district);
            return;
        }
        LOGGER.info("No district: {} selection is needed", district);
    }

    public Set<String> getAvailableSubDistricts()
    {
        openSubDiscticts();
        List<WebElement> subDisctricts = waitForSubDistrictList();
        Set<String> results = subDisctricts.stream().map(e -> e.findElement(By.tagName("label")).getText())
                .collect(Collectors.toSet());
        openSubDiscticts();
        return results;
    }

    private void openSubDiscticts()
    {
        List<WebElement> disctricts = searchActions
                .waitForElements(By.xpath("(//span[@class='re-GeographicSearch-listTriggerText'])[5]"), 10);
        clickActions.click(disctricts);
    }

    private List<WebElement> waitForSubDistrictList()
    {
        LOGGER.debug("Waiting for subDistricts dropDown appear...");
        return searchActions.waitForElements(By.xpath("(//ol[@class='re-GeographicSearch-list'])[5]//li/a"), 5);
    }

    private List<WebElement> waitForDistrictList()
    {
        LOGGER.debug("Waiting for Districts dropDown appear...");
        return searchActions.waitForElements(By.xpath("(//ol[@class='re-GeographicSearch-list'])[4]//li/a"), 5);
    }

    private void clickApplySubDistrics(List<WebElement> subDisctricts)
    {
        LOGGER.debug("Submitting SubDistricts filter form...");
        List<WebElement> searchButton = searchActions.findElementsByXpath(
                "(//li[@class='re-GeographicSearch-level'])[5]//div[@class='re-GeographicSearch-submit']//button");
        clickActions.click(searchButton);
        List<WebElement> dropDown = searchActions.findElementsByXpath("(//li[@class='re-GeographicSearch-level'])[5]");
        searchActions.waitForElementsDisappear(searchButton, 5);
        searchActions.waitForElementsDisappear(dropDown, 5);
        LOGGER.debug("SubDistricts Filter form has been submited...");
    }

    private void clickApplyDistrics(List<WebElement> subDisctricts)
    {
        LOGGER.debug("Submitting Districts filter form...");
        List<WebElement> searchButton = searchActions.findElementsByXpath(
                "(//li[@class='re-GeographicSearch-level'])[4]//div[@class='re-GeographicSearch-submit']//button");
        clickActions.click(searchButton);
        List<WebElement> dropDown = searchActions.findElementsByXpath("(//li[@class='re-GeographicSearch-level'])[4]");
        searchActions.waitForElementsDisappear(searchButton, 5);
        searchActions.waitForElementsDisappear(dropDown, 5);
        LOGGER.debug("Districts Filter form has been submited...");
    }

    private void selectItemFromList(List<WebElement> itemsList, String itemName)
    {
        LOGGER.debug("Selecting item <{}> from list", itemName);
        for (WebElement item : itemsList)
        {
            System.out.println(item.getText());
            if (item.getText().contains(itemName))
            {
                LOGGER.info("SubDisctrict item found: {} with text: {}", item, item.getText());
                clickActions.click(item);
                LOGGER.debug("SubDisctrict item clicked: {} ", itemName);
                return;
            }
        }
        LOGGER.error("Could not find subDistrict item with text: {}", itemName);
    }

    private void unselectSubDistrics(List<WebElement> subDisctricts)
    {
        LOGGER.debug("Unchecking all subDistricts...");
        for (WebElement item : subDisctricts)
        {
            List<WebElement> checked = searchActions.findElementsByXpath(item, "//input[@checked]");
            if (!checked.isEmpty())
            {
                LOGGER.debug("Found checked item: {} - unchecking it", item.getText());
                clickActions.click(checked.get(0).findElement(By.xpath("..")));
            }
        }
    }
}
