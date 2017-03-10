package com.idealista.scraper.scraping;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.ui.page.AdvertismentPage;
import com.idealista.scraper.webdriver.INavigateActions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdvertismentExtractorFactory implements IAdvertismentExtractorFactory
{
    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private AdvertismentPage page;

    @Override
    public AdvertismentExtractor create(Category category)
    {
        return new AdvertismentExtractor(category, navigateActions, page);
    }
}
