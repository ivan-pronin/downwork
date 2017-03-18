package com.idealista.scraper.scraping.advextractor;

import com.idealista.scraper.model.Category;

public interface IAdvertisementExtractorFactory
{
    IAdvertisementExtractor create(Category category);
}
