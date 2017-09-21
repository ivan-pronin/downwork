package com.idealista.scraper.scraping.category.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

import com.idealista.scraper.ui.actions.SearchActions;

public class FotocasaAdUrlsFilter implements IAdUrlsFilter
{
    private static final Logger LOGGER = LogManager.getLogger(FotocasaAdUrlsFilter.class);

    private SearchActions searchActions;

    @Override
    public List<WebElement> filterAdUrls(List<WebElement> advertisementElements)
    {
        List<WebElement> filteredList = new ArrayList<>();
        filteredList.addAll(advertisementElements.stream().filter(e -> searchActions.findElementsByXpath(e,
                "//descendant::*[contains(@class,'re-Card-promotionLogo') or contains(@class,'clientLogo') or contains(@class,'property-list-agency')]")
                .isEmpty()).collect(Collectors.toList()));
        LOGGER.debug("After filtering from {} items only {} items remain", advertisementElements.size(),
                filteredList.size());
        return filteredList;
    }

    @Override
    public void setSearchActions(SearchActions searchActions)
    {
        this.searchActions = searchActions;
    }
}
