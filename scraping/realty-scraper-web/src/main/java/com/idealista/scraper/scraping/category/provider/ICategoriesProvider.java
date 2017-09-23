package com.idealista.scraper.scraping.category.provider;

import java.util.Set;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.search.GenericSearchFilterContext;

public interface ICategoriesProvider
{
    Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext);
}
