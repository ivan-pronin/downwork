package com.idealista.scraper.scraping;

import com.idealista.scraper.model.Category;

public interface ISearchPageProcessorFactory
{

    SearchPageProcessor create(Category category);

}
