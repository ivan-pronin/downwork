package com.idealista.scraper.scraping.searchpage.factory;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.searchpage.processor.ISeachPageProcessor;

public interface ISearchPageProcessorFactory
{
    ISeachPageProcessor create(Category category);
}
