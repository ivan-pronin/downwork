package com.idealista.scraper.pisos;

import java.net.URL;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.advextractor.IAdvertisementExtractorFactory;
import com.idealista.scraper.scraping.advextractor.PisosAdvertisementExtractor;
import com.idealista.scraper.service.IScrappingService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class PisosAdsExtractorTests
{
    @Autowired
    private IAdvertisementExtractorFactory advertismentExtractorFactory;
    
    @Test
    public void testName() throws Exception
    {
        String url = "https://www.pisos.com/comprar/piso-calella_centro_urbano-945856909133764_109700/";
        Category cat = new Category(new URL(url), "state", "type", "Naves");
        PisosAdvertisementExtractor extractor = (PisosAdvertisementExtractor) advertismentExtractorFactory.create(cat);
        
        Advertisement advertisment = extractor.call();
        
    }
}
