package com.idealista.scraper.service;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.scraping.advextractor.IAdvertisementExtractorFactory;
import com.idealista.scraper.scraping.category.AdUrlsFinder;
import com.idealista.scraper.scraping.category.FoundUrlsManager;
import com.idealista.scraper.scraping.category.IAdUrlsFinder;
import com.idealista.scraper.scraping.category.chooser.ICategoriesChooser;
import com.idealista.scraper.webdriver.INavigateActions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class FotocasaScrappingService implements IScrappingService
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaScrappingService.class);
    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.FOTOCASA;

    private BlockingQueue<Future<Advertisement>> advertismentExtractorResults;
    private BlockingQueue<URL> advertismentUrlsInProgress = new LinkedBlockingQueue<>();

    @Value("${operation}")
    private String userOperation;

    @Value("${searchString}")
    private String userSearchString;

    @Value("${newHomes}")
    private boolean newHomes;

    @Value(value = "${maxAdsToProcess}")
    private int maxAdsToProcess;
    
    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${districts}') }")
    private Set<String> userDistricts;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private ICategoriesChooser categoriesChooser;

    @Autowired
    private IAdUrlsFinder adUrlsFinder;

    @Autowired
    private FoundUrlsManager foundUrlsManager;
    
    @Autowired
    private ExecutorServiceProvider executor;
    
    @Autowired
    private IAdvertisementExtractorFactory advertismentExtractorFactory;

    @Override
    public void scrapSite() throws InterruptedException, MalformedURLException
    {
        navigateActions.get(new URL(SCRAP_TARGET.getMainPageUrl()));
        SearchAttributes searchAttributes = new SearchAttributes();
        searchAttributes.setOperations(new HashSet<>(Arrays.asList(userOperation)));
        searchAttributes.setSearchString(userSearchString);
        FilterAttributes filterAttributes = new FilterAttributes();
        filterAttributes.setNewHomes(newHomes);
        filterAttributes.setDisctricts(userDistricts);
        GenericSearchFilterContext context = new GenericSearchFilterContext();
        context.setSearchAttributes(searchAttributes);
        context.setFilterAttributes(filterAttributes);
        IScrappingService.printAttributesInfo(LOGGER, searchAttributes, filterAttributes);

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

    @Override
    public void setAdvertismentExtractorResults(BlockingQueue<Future<Advertisement>> advertismentExtractorResults)
    {
        this.advertismentExtractorResults = advertismentExtractorResults;

    }

    @Override
    public BlockingQueue<URL> getAdvertismentUrlsInProgress()
    {
        return advertismentUrlsInProgress;
    }
}
