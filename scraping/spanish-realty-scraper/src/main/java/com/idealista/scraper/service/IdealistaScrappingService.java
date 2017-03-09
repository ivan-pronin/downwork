package com.idealista.scraper.service;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.SearchAttributes;
import com.idealista.scraper.scraping.IAdvertismentExtractorFactory;
import com.idealista.scraper.scraping.search.AdUrlsFinder;
import com.idealista.scraper.scraping.search.CategoriesChooser;
import com.idealista.scraper.scraping.search.IAdUrlsFinder;
import com.idealista.scraper.ui.page.StartPage;
import com.idealista.scraper.webdriver.NavigateActions;
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
    private static final String IDEALISTA_COM_EN = "https://www.idealista.com/en/";

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private ExecutorServiceProvider executor;

    @Autowired
    private IAdUrlsFinder adUrlsFinder;

    @Autowired
    private CategoriesChooser chooser;

    @Autowired
    private IAdvertismentExtractorFactory advertismentExtractorFactory;

    private BlockingQueue<Future<Advertisment>> advertismentExtractorResults;
    private BlockingQueue<URL> advertismentUrlsInProgress = new LinkedBlockingQueue<>();

    @Value(value = "${maxAdsToProcess}")
    private int maxIterations;

    public void scrapSite(String userOperation, String userTypology, String userLocation)
            throws InterruptedException, MalformedURLException
    {
        WebDriver driver = webDriverProvider.get();
        NavigateActions navigateActions = new NavigateActions(driver);
        navigateActions.get(new URL(IDEALISTA_COM_EN));

        StartPage startPage = new StartPage(driver);
        SearchAttributes searchAttributes = startPage.getSearchAttributes(userOperation, userTypology, userLocation);

        LOGGER.info(" === === Printing parsed SearchAttributes === === ");
        LOGGER.info(searchAttributes);
        LOGGER.info(" === === === === === === ===  === === === === === ");
        LOGGER.info("");
        Set<Category> categoriesBaseUrls = getCategoriesUrls(searchAttributes);

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

    private Set<Category> getCategoriesUrls(SearchAttributes searchAttributes) throws InterruptedException
    {
        Set<String> userOperations = searchAttributes.getOperations();
        Set<String> userTypologies = searchAttributes.getTypologies();
        Set<String> userLocations = searchAttributes.getLocations();
        StartPage startPage = new StartPage(webDriverProvider.get());

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
                            results.add(chooser.new CategoryByOperationTypeLocation(operation, typology, location));
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
