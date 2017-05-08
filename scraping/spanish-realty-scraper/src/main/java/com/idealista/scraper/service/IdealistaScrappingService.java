package com.idealista.scraper.service;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.model.filter.IFilterAttributesFactory;
import com.idealista.scraper.model.parser.ISearchAttributesParser;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.IdealistaSearchAttributes;
import com.idealista.scraper.model.search.SearchAttributes;
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

@Profile("idealista")
@Component
public class IdealistaScrappingService implements IScrappingService
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaScrappingService.class);
    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.IDEALISTA;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${operation}') }")
    private Set<String> userOperation;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${typology}') }")
    private Set<String> userTypology;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${location}') }")
    private Set<String> userLocation;

    @Value("${province}")
    private String userProvince;

    @Value("${publicationDateFilter}")
    private String publicationDateFilter;

    @Value(value = "${maxAdsToProcess}")
    private int maxAdsToProcess;

    @Autowired
    private ExecutorServiceProvider executor;

    @Autowired
    private IAdUrlsFinder adUrlsFinder;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private IFilterAttributesFactory filterAttributesFactory;

    @Autowired
    private ISearchAttributesParser searchAttributesParser;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ICategoriesChooser categoriesChooser;

    @Autowired
    private FoundUrlsManager foundUrlsManager;

    @Autowired
    private IAdvertisementExtractorFactory advertismentExtractorFactory;

    private BlockingQueue<Future<Advertisement>> advertismentExtractorResults;
    private BlockingQueue<URL> advertismentUrlsInProgress = new LinkedBlockingQueue<>();

    @Override
    public void scrapSite() throws InterruptedException, MalformedURLException
    {
        navigateActions.get(new URL(SCRAP_TARGET.getMainPageLocalizedUrl(appConfig.getLanguage())));
        SearchAttributes searchAttributes = searchAttributesParser.parseSearchAttributes(getAttributesMap());
        FilterAttributes filterAttributes = filterAttributesFactory.create(publicationDateFilter);
        GenericSearchFilterContext context = new GenericSearchFilterContext();
        context.setSearchAttributes(searchAttributes);
        context.setFilterAttributes(filterAttributes);
        context.setProvince(userProvince);
        LOGGER.info(" === === Printing parsed SearchAttributes === === ");
        LOGGER.info(searchAttributes);
        LOGGER.info(" === === === === === === ===  === === === === === ");
        LOGGER.info("");

        Set<Category> categoriesBaseUrls = categoriesChooser.getCategoriesUrls(context);

        ((AdUrlsFinder) adUrlsFinder).setCategoriesBaseUrls(categoriesBaseUrls);
        Set<Category> adUrlsToProcess = adUrlsFinder.findNewAdUrlsAmount(maxAdsToProcess);

        adUrlsToProcess = foundUrlsManager.getNewestAdsById(adUrlsToProcess);
        
        LOGGER.debug("Printing all URLS that will be scrapped in this session. Total count: {}", adUrlsToProcess.size());
        adUrlsToProcess.forEach(e -> LOGGER.debug(e.getUrl()));
        
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

    private Map<IGenericSearchAttributes, Set<String>> getAttributesMap()
    {
        return new HashMap<IGenericSearchAttributes, Set<String>>()
        {
            private static final long serialVersionUID = -4234938230147805771L;
            {
                put(IdealistaSearchAttributes.OPERATION, userOperation);
                put(IdealistaSearchAttributes.TYPOLOGY, userTypology);
                put(IdealistaSearchAttributes.LOCATION, userLocation);
            }
        };
    }

    @Override
    public void setAdvertismentExtractorResults(BlockingQueue<Future<Advertisement>> advertismentExtractorResults)
    {
        this.advertismentExtractorResults = advertismentExtractorResults;
    }

    public BlockingQueue<URL> getAdvertismentUrlsInProgress()
    {
        return advertismentUrlsInProgress;
    }
}
