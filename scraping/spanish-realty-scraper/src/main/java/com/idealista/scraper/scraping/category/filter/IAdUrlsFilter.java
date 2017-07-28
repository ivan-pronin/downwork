package com.idealista.scraper.scraping.category.filter;

import java.util.List;

import com.idealista.scraper.ui.actions.SearchActions;

import org.openqa.selenium.WebElement;

public interface IAdUrlsFilter
{
    List<WebElement> filterAdUrls(List<WebElement> advertisementElements);
    
    void setSearchActions(SearchActions searchActions);
}
