package com.idealista.scraper.ui.page;

import com.idealista.scraper.model.filter.FilterAttributes;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Component
public class SearchPage extends BasePage
{
    private static final Logger LOGGER = LogManager.getLogger(SearchPage.class);

    public void applyPublicationDateFilter(FilterAttributes filterAttributes)
    {
        LOGGER.info("Applying search filter: {}", filterAttributes);
        List<WebElement> filterForm = searchActions.findElementsById("filter-form");
        String xpath = "//label[@class='input-radio']//a[contains(@href,'%s')]";
        List<WebElement> publicationDateFilter = searchActions.findElementsByXpath(filterForm,
                String.format(xpath, filterAttributes.getPublicationDateFilter().getRelativeUrl()));
        clickActions.click(publicationDateFilter);
        searchActions.waitForElement(By.xpath("//div[@class='listing-loading-content']"), 5);
        searchActions.waitForElementDisappear(By.xpath("//div[@class='listing-loading-content']"), 5);
        LOGGER.info("Filter has been applied");
    }

    public void selectProvince(String province)
    {
        if (StringUtils.isEmpty(province))
        {
            LOGGER.info("No province was specified, nothing to select ...");
            return;
        }
        LOGGER.info("Selecting province <{}>", province);
        List<WebElement> provincesButton = searchActions
                .findElementsByXpath("//span[@class='breadcrumb-title icon-arrow-dropdown-after']");
        clickActions.click(provincesButton);
        WebElement dropDown = searchActions.waitForElement(By.xpath("//div[@class='breadcrumb-subitems']"), 5);
        List<WebElement> provincesItems = searchActions.findElementsByXpath(Arrays.asList(dropDown), "//ul//li//a");
        for (WebElement provinceItem : provincesItems)
        {
            if (provinceItem.getText().equalsIgnoreCase(province))
            {
                clickActions.click(provinceItem);
                if (waitForProvinceToLoad(province))
                {
                    LOGGER.info("Province has been selected!");
                }
                return;
            }
        }
        LOGGER.warn("No province was selected");
    }

    private boolean waitForProvinceToLoad(String province)
    {
        String xpath = "//li[@class='current-level']//span[contains(.,'%s')]";
        WebElement currentProvince = searchActions.waitForElement(By.xpath(String.format(xpath, province)), 10);
        if (currentProvince == null)
        {
            LOGGER.error("Failed to load province: {}", province);
            return false;
        }
        return true;
    }
}
