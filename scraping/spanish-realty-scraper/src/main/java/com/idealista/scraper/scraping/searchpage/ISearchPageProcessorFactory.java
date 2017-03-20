package com.idealista.scraper.scraping.searchpage;

import com.idealista.scraper.model.Category;

public interface ISearchPageProcessorFactory
{
    ISeachPageProcessor create(Category category);
}
