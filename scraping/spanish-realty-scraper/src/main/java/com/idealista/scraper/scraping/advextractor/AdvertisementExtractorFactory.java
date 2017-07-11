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
        AbstractAdvertisementExtractor extractor = null;
        switch (appConfig.getScrapTarget())
        {
            case IDEALISTA:
                extractor = new IdealistaAdvertisementExtractor(category);
                ((IdealistaAdvertisementExtractor) extractor).setLanguage(appConfig.getLanguage());
                break;
            case VIBBO:
                extractor = new VibboAdvertisementExtractor(category);
                break;
            case FOTOCASA:
                extractor = new FotocasaAdvertisementExtractor(category);
                break;
            case PISOS:
                extractor = new PisosAdvertisementExtractor(category);
                break;
            default:
                break;
        }
        extractor.setNavigateActions(navigateActions);
        return extractor;
    }
}
