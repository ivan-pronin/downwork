package com.idealista.scraper.ui.page;

import com.idealista.scraper.service.model.FilterAttributes;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

@Component
public class SearchPage extends BasePage
{
    private static final Logger LOGGER = LogManager.getLogger(SearchPage.class);

    public void applyPublicationDateFilter(FilterAttributes filterAttributes)
    {
        LOGGER.info("Applying search filter: {}", filterAttributes);
        List<WebElement> filterForm = searchActions.findElementsById(Collections.emptyList(), "filter-form");
        String xpath = "//label[@class='input-radio']//a[contains(@href,'%s')]";
        List<WebElement> publicationDateFilter = searchActions.findElementsByXpath(filterForm,
                String.format(xpath, filterAttributes.getPublicationDateFilter().getRelativeUrl()));
        clickActions.click(publicationDateFilter);
        searchActions.waitForElement(By.xpath("//div[@class='listing-loading-content']"), 5);
        searchActions.waitForElementDisappear(By.xpath("//div[@class='listing-loading-content']"), 5);
        LOGGER.info("Filter applied");
    }
}
