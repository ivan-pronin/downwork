package com.idealista.scraper.scraping.category.filter;

import java.util.List;

import org.openqa.selenium.WebElement;

import com.idealista.scraper.ui.actions.SearchActions;

public interface IAdUrlsFilter
{
    List<WebElement> filterAdUrls(List<WebElement> advertisementElements);

    void setSearchActions(SearchActions searchActions);
}
