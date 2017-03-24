package com.idealista.scraper.service;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.parser.ISearchAttributesParser;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.model.search.VibboSearchAttributes;
import com.idealista.scraper.scraping.advextractor.IAdvertisementExtractorFactory;
import com.idealista.scraper.scraping.category.AdUrlsFinder;
import com.idealista.scraper.scraping.category.FoundUrlsManager;
import com.idealista.scraper.scraping.category.IAdUrlsFinder;
import com.idealista.scraper.scraping.category.ICategoriesChooser;
import com.idealista.scraper.webdriver.INavigateActions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

@Profile("vibbo")
@Component
public class VibboScrappingService implements IScrappingService
{
    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.VIBBO;
    private static final Logger LOGGER = LogManager.getLogger(VibboScrappingService.class);

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${typology}') }")
    private Set<String> userTypology;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${operation}') }")
    private Set<String> userOperation;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${propertyType}')}")
    private Set<String> userPropertyType;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${advertiser}')}")
    private Set<String> userAdvertiser;

    @Value(value = "${maxAdsToProcess}")
    private int maxAdsToProcess;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private ISearchAttributesParser searchAttributesParser;

    @Autowired
    private ICategoriesChooser categoriesChooser;

    @Autowired
    private IAdUrlsFinder adUrlsFinder;

    @Autowired
    private ExecutorServiceProvider executor;

    @Autowired
    private IAdvertisementExtractorFactory advertismentExtractorFactory;
    
    @Autowired
    private FoundUrlsManager foundUrlsManager;

    private BlockingQueue<Future<Advertisement>> advertismentExtractorResults;
    private BlockingQueue<URL> advertismentUrlsInProgress = new LinkedBlockingQueue<>();

    @Override
    public BlockingQueue<URL> getAdvertismentUrlsInProgress()
    {
        return advertismentUrlsInProgress;
    }

    @Override
    public void scrapSite() throws InterruptedException, MalformedURLException
    {
        navigateActions.get(new URL(SCRAP_TARGET.getMainPageUrl()));
        SearchAttributes searchAttributes = searchAttributesParser.parseSearchAttributes(getAttributesMap());
        LOGGER.info(" === === Printing parsed SearchAttributes === === ");
        LOGGER.info(searchAttributes);
        LOGGER.info(" === === === === === === ===  === === === === === ");
        LOGGER.info("");
        GenericSearchFilterContext context = new GenericSearchFilterContext();
        context.setSearchAttributes(searchAttributes);
        Set<Category> categoriesBaseUrls = categoriesChooser.getCategoriesUrls(context);

        ((AdUrlsFinder) adUrlsFinder).setCategoriesBaseUrls(categoriesBaseUrls);
        Set<Category> adUrlsToProcess = adUrlsFinder.findNewAdUrlsAmount(maxAdsToProcess);

        adUrlsToProcess = foundUrlsManager.getNewestAdsById(adUrlsToProcess);
        
        Iterator<Category> iterator = adUrlsToProcess.iterator();
        for (int i = 0; i < maxAdsToProcess; i++)
        {
            if (iterator.hasNext())
            {
                Category page = iterator.next();
                advertismentExtractorResults
                        .put(executor.getExecutor().submit(advertismentExtractorFactory.create(page)));
                advertismentUrlsInProgress.put(page.getUrl());
            }
        }
    }

    @Override
    public void setAdvertismentExtractorResults(BlockingQueue<Future<Advertisement>> advertismentExtractorResults)
    {
        this.advertismentExtractorResults = advertismentExtractorResults;
    }

    private Map<IGenericSearchAttributes, Set<String>> getAttributesMap()
    {
        return new HashMap<IGenericSearchAttributes, Set<String>>()
        {
            private static final long serialVersionUID = -7478768386290258812L;
            {
                put(VibboSearchAttributes.TYPOLOGY, userTypology);
                put(VibboSearchAttributes.OPERATION, userOperation);
                put(VibboSearchAttributes.PROPERTY_TYPE, userPropertyType);
                put(VibboSearchAttributes.ADVERTISER, userAdvertiser);
            }
        };
    }
}
