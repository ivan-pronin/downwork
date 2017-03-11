package com.idealista.scraper.service;

import com.idealista.scraper.RealtyApp;
import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.SearchAttributes;
import com.idealista.scraper.scraping.IAdvertismentExtractorFactory;
import com.idealista.scraper.scraping.search.AdUrlsFinder;
import com.idealista.scraper.scraping.search.CategoriesChooser;
import com.idealista.scraper.scraping.search.IAdUrlsFinder;
import com.idealista.scraper.service.model.FilterAttributes;
import com.idealista.scraper.service.model.IFilterAttributesFactory;
import com.idealista.scraper.ui.page.StartPage;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

@Component
public class IdealistaScrappingService
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaScrappingService.class);

    @Value("${operation}")
    private String userOperation;

    @Value("${typology}")
    private String userTypology;

    @Value("${location}")
    private String userLocation;

    @Value("${province}")
    private String userProvince;

    @Value("${publicationDateFilter}")
    private String publicationDateFilter;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private ExecutorServiceProvider executor;

    @Autowired
    private IAdUrlsFinder adUrlsFinder;

    @Autowired
    private CategoriesChooser chooser;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private IAdvertismentExtractorFactory advertismentExtractorFactory;

    @Autowired
    private IFilterAttributesFactory filterAttributesFactory;

    @Autowired
    private StartPage startPage;
    
    @Autowired
    private RealtyApp realtyApp;

    private BlockingQueue<Future<Advertisment>> advertismentExtractorResults;
    private BlockingQueue<URL> advertismentUrlsInProgress = new LinkedBlockingQueue<>();

    @Value(value = "${maxAdsToProcess}")
    private int maxIterations;

    public void scrapSite() throws InterruptedException, MalformedURLException
    {
        WebDriver driver = webDriverProvider.get();
        navigateActions.get(new URL(realtyApp.getMainLocalizedPageUrl()));

        startPage.setWebDriver(driver);
        SearchAttributes searchAttributes = startPage.getSearchAttributes(userOperation, userTypology, userLocation);
        FilterAttributes filterAttributes = filterAttributesFactory.create(publicationDateFilter);

        LOGGER.info(" === === Printing parsed SearchAttributes === === ");
        LOGGER.info(searchAttributes);
        LOGGER.info(" === === === === === === ===  === === === === === ");
        LOGGER.info("");
        Set<Category> categoriesBaseUrls = getCategoriesUrls(searchAttributes, filterAttributes, userProvince);

        ((AdUrlsFinder) adUrlsFinder).setCategoriesBaseUrls(categoriesBaseUrls);
        Set<Category> adUrlsToProcess = adUrlsFinder.findNewAdUrlsAmount(maxIterations);

        Iterator<Category> iterator = adUrlsToProcess.iterator();
        for (int i = 0; i < maxIterations; i++)
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

    private Set<Category> getCategoriesUrls(SearchAttributes searchAttributes, FilterAttributes filterAttributes,
            String province) throws InterruptedException
    {
        Set<String> userOperations = searchAttributes.getOperations();
        Set<String> userTypologies = searchAttributes.getTypologies();
        Set<String> userLocations = searchAttributes.getLocations();
        startPage.setWebDriver(webDriverProvider.get());

        Queue<Callable<Category>> results = new ConcurrentLinkedQueue<>();

        for (String operation : userOperations)
        {
            startPage.selectOperation(operation);
            Set<String> availableTypologies = startPage.getAvailableTypologies();

            for (String typology : availableTypologies)
            {
                if (userTypologies.contains(typology))
                {
                    startPage.selectTypology(typology);
                    Set<String> availableLocations = startPage.getAvailableLocations();
                    for (String location : availableLocations)
                    {
                        if (userLocations.contains(location))
                        {
                            startPage.selectLocation(location);
                            results.add(chooser.new CategoryBySearchAndFilterAttributes(operation, typology, location,
                                    filterAttributes, province));
                        }
                    }
                }
            }
        }

        List<Future<Category>> categories = executor.getExecutor().invokeAll(results);
        return categories.stream().map(t ->
        {
            try
            {
                return t.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                LOGGER.error("Error while retrieving category task result: {}", e);
                return null;
            }
        }).collect(Collectors.toSet());
    }

    public BlockingQueue<URL> getAdvertismentUrlsInProgress()
    {
        return advertismentUrlsInProgress;
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }

    public void setExecutor(ExecutorServiceProvider executor)
    {
        this.executor = executor;
    }

    public void setAdUrlsFinder(IAdUrlsFinder adUrlsFinder)
    {
        this.adUrlsFinder = adUrlsFinder;
    }

    public void setChooser(CategoriesChooser chooser)
    {
        this.chooser = chooser;
    }

    public void setMaxIterations(int maxIterations)
    {
        this.maxIterations = maxIterations;
    }

    public void setAdvertismentExtractorResults(BlockingQueue<Future<Advertisment>> advertismentExtractorResults)
    {
        this.advertismentExtractorResults = advertismentExtractorResults;
    }

    public void setAdvertismentExtractorFactory(IAdvertismentExtractorFactory advertismentExtractorFactory)
    {
        this.advertismentExtractorFactory = advertismentExtractorFactory;
    }
}
