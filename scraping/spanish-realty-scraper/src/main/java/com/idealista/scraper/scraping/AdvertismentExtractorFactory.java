package com.idealista.scraper.scraping;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdvertismentExtractorFactory implements IAdvertismentExtractorFactory
{
    @Autowired
    private WebDriverProvider webDriverProvider;

    @Override
    public AdvertismentExtractor create(Category category)
    {
        return new AdvertismentExtractor(webDriverProvider, category);
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }
}
