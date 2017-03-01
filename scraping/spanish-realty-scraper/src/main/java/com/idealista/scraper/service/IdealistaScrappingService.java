package com.idealista.scraper.service;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.page.AdvertismentExtractor;
import com.idealista.scraper.page.Paginator;
import com.idealista.scraper.page.SearchPageProcessor;
import com.idealista.scraper.page.StartPage;
import com.idealista.scraper.search.CategoriesChooser;
import com.idealista.scraper.search.Category;
import com.idealista.scraper.search.SearchAttributes;
import com.idealista.scraper.util.PropertiesLoader;
import com.idealista.scraper.webdriver.NavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.stream.Collectors;

public class IdealistaScrappingService
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaScrappingService.class);
    private static final String IDEALISTA_COM_EN = "https://www.idealista.com/en/";

    private WebDriverProvider webDriverProvider;
    private ExecutorService executor;
    private Properties props = PropertiesLoader.getProperties();
    private BlockingQueue<Future<Advertisment>> advertismentExtractorResults;
    private BlockingQueue<URL> advertismentUrlsInProgress = new LinkedBlockingQueue<>();

    public IdealistaScrappingService(WebDriverProvider webDriverProvider,
            BlockingQueue<Future<Advertisment>> advertismentExtractorResults)
    {
        this.webDriverProvider = webDriverProvider;
        this.advertismentExtractorResults = advertismentExtractorResults;
        executor = ExecutorServiceProvider.getExecutor();
    }

    public void scrapSite(String userOperation, String userTypology, String userLocation) throws InterruptedException, MalformedURLException
    {
        int maxIterations = Integer.parseInt(props.getProperty("maxAdsToProcess", "100"));
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

        Paginator paginator = new Paginator();
        Queue<Category> searchPagesToProcess = new ConcurrentLinkedQueue<>();

        categoriesBaseUrls.forEach(e -> searchPagesToProcess.addAll(paginator.getAllPageUrls(driver, e)));

        Queue<Callable<Set<Category>>> tasks = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < (maxIterations / 30) + 1; i++)
        {
            if (!searchPagesToProcess.isEmpty())
            {
                Category page = searchPagesToProcess.poll();
                tasks.add(new SearchPageProcessor(webDriverProvider, page));
            }
        }
        List<Future<Set<Category>>> taskResults = executor.invokeAll(tasks);

        Set<Category> adUrls = new HashSet<>();
        taskResults.forEach(e ->
        {
            try
            {
                adUrls.addAll(e.get());
            }
            catch (InterruptedException | ExecutionException e2)
            {
                LOGGER.error("Error while retrieving ad url from category task: {}", e);
            }
        });

        Iterator<Category> iterator = adUrls.iterator();
        for (int i = 0; i < maxIterations; i++)
        {
            if (iterator.hasNext())
            {
                Category page = iterator.next();
                AdvertismentExtractor extractor = new AdvertismentExtractor(webDriverProvider, page);
                advertismentExtractorResults.put(executor.submit(extractor));
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

        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);

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

        List<Future<Category>> categories = executor.invokeAll(results);
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
}
