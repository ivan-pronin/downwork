package com.idealista.scraper.ui.page.idealista;

import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.ui.page.BasePage;
import com.idealista.scraper.ui.page.ISearchPage;

@Component
public class IdealistaSearchPage extends BasePage implements ISearchPage
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaSearchPage.class);

    public void applyPublicationDateFilter(FilterAttributes filterAttributes)
    {
        LOGGER.info("Applying search filter: {}", filterAttributes);
        List<WebElement> filterForm = searchActions.findElementsById("filter-form");
        String xpath = "//label[@class='input-radio']//input[@value='%s']/following-sibling::span";
        List<WebElement> publicationDateFilter = searchActions.findElementsByXpath(filterForm,
                String.format(xpath, filterAttributes.getPublicationDateFilter().getRelativeUrl()));
        clickActions.scrollToElement(publicationDateFilter.get(0));
        clickActions.scrollScreenDownByPixels(-200);
        clickActions.click(publicationDateFilter);
        waitActions.waitForElement(By.xpath("//div[@class='listing-loading-content']"), 5);
        waitActions.waitForElementDisappear(By.xpath("//div[@class='listing-loading-content']"), 5);
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
        WebElement dropDown = waitActions.waitForElement(By.xpath("//div[@class='breadcrumb-subitems']"), 5);
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
        WebElement currentProvince = waitActions.waitForElement(By.xpath(String.format(xpath, province)), 10);
        if (currentProvince == null)
        {
            LOGGER.error("Failed to load province: {}", province);
            return false;
        }
        return true;
    }
}
