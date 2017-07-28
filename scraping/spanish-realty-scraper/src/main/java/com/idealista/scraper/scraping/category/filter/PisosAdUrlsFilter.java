package com.idealista.scraper.scraping.category.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.idealista.scraper.ui.actions.SearchActions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;

public class PisosAdUrlsFilter implements IAdUrlsFilter
{
    private static final Logger LOGGER = LogManager.getLogger(PisosAdUrlsFilter.class);

    private SearchActions searchActions;

    @Override
    public List<WebElement> filterAdUrls(List<WebElement> advertisementElements)
    {
        List<WebElement> filteredList = new ArrayList<>();
        filteredList.addAll(advertisementElements
                .stream().filter(e -> searchActions
                        .findElementsByXpath(e, "//descendant::*[contains(@class,'anunciante-logo')]").isEmpty())
                .collect(Collectors.toList()));
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
