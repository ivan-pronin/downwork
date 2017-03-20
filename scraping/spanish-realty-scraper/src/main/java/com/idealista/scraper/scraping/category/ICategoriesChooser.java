package com.idealista.scraper.scraping.category;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.search.GenericSearchFilterContext;

import java.util.Set;

public interface ICategoriesChooser
{
    Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext);
}
