package com.idealista.scraper.scraping;

import com.idealista.scraper.model.Category;

public interface IAdvertismentExtractorFactory
{

    AdvertismentExtractor create(Category category);

}
