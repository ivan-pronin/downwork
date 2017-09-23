package com.idealista.scraper.ui.page.vibbo;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.idealista.scraper.model.search.CategoryChoosingAttribute;
import com.idealista.scraper.ui.page.BasePage;
import com.idealista.scraper.ui.page.IStartPage;

@Component
public class VibboStartPage extends BasePage implements IStartPage
{
    private static final Logger LOGGER = LogManager.getLogger(VibboStartPage.class);

    public void selectOptionsAndStartSearch(CategoryChoosingAttribute attribute)
    {
        LOGGER.info("Selecting search options: {}", attribute);
        selectTypology(attribute.getTypology());
        selectOperation(attribute.getOperation());
        selectPropertyType(attribute.getPropertyType());
        selectAdvertiser(attribute.getAdvertiser());
        clickApply();
    }

    private void clickApply()
    {
        // Need to wait until all form elements are loaded as one of the criterias of
        // filter being applied and page results loaded
        clickActions.click(searchActions.findElementsById("sb_searchbutton_filter"));
        int waitSeconds = 5;
        waitActions.waitForElement(By.id("parent_category"), waitSeconds);
        waitActions.waitForElement(By.id("c"), waitSeconds);
        waitActions.waitForElement(By.id("st"), waitSeconds);
        waitActions.waitForElement(By.id("sb_f_input"), waitSeconds);
        waitActions.waitForElement(By.id("sb_searchbutton_filter"), waitSeconds);
        waitActions.waitForElement(By.id("clearFiltersDesktop"), waitSeconds);
    }

    private void selectAdvertiser(String advertiser)
    {
        selectSearchOption(advertiser, "advertiser", "sb_f_input");
    }

    private void selectOperation(String operation)
    {
        selectSearchOption(operation, "operation", "sb_transaccion");
    }

    private void selectPropertyType(String propertyType)
    {
        selectSearchOption(propertyType, "propertyType", "sb_itype");
    }

    private void selectSearchOption(String optionValue, String optionName, String elementId)
    {
        if (StringUtils.isEmpty(optionValue))
        {
            LOGGER.warn("Specified <{}> is null, skipping...", optionName);
            return;
        }
        WebElement operationCombo = waitActions.waitForElement(By.id(elementId), 5);
        if (operationCombo != null)
        {
            List<WebElement> options = waitActions
                    .waitForElements(By.xpath(String.format("//*[@id='%s']//option", elementId)), 5);
            for (WebElement option : options)
            {
                if (optionValue.equalsIgnoreCase(option.getText()))
                {
                    clickActions.click(option);
                    LOGGER.info("Clicked on <{}> option with text: {}", optionName, optionValue);
                    return;
                }
            }
        }
        LOGGER.warn("Could not find <{}> with value: {}", optionName, optionValue);
    }

    private void selectTypology(String typology)
    {
        selectSearchOption(typology, "typology", "category_container");
    }
}
