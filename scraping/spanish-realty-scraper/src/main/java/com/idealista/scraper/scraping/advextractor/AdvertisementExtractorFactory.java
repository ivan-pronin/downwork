package com.idealista.scraper.scraping.advextractor;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.webdriver.INavigateActions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AdvertisementExtractorFactory implements IAdvertisementExtractorFactory
{
    @Autowired
    private INavigateActions navigateActions;
    
    @Autowired
    private AppConfig appConfig;
    
    @Override
    public IAdvertisementExtractor create(Category category)
    {
        switch (appConfig.getScrapTarget())
        {
            case IDEALISTA:
                IdealistaAdvertisementExtractor idealistaExtractor = new IdealistaAdvertisementExtractor();
                idealistaExtractor.setCategory(category);
                idealistaExtractor.setNavigateActions(navigateActions);
                idealistaExtractor.setLanguage(appConfig.getLanguage());
                return idealistaExtractor;
            case VIBBO:
                VibboAdvertisementExtractor vibboExtractor = new VibboAdvertisementExtractor();
                vibboExtractor.setCategory(category);
                vibboExtractor.setNavigateActions(navigateActions);
                return vibboExtractor;
            default:
                break;
        }
        return null;
    }
}
