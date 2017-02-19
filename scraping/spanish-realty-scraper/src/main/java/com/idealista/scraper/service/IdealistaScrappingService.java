package com.idealista.scraper.service;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.model.RealtyType;
import com.idealista.scraper.page.AdvertismentExtractor;
import com.idealista.scraper.page.Paginator;
import com.idealista.scraper.page.SearchPageProcessor;
import com.idealista.scraper.page.StartPage;
import com.idealista.scraper.search.CategoriesChooser;
import com.idealista.scraper.search.SearchAttribute;
import com.idealista.scraper.util.PropertiesLoader;
import com.idealista.scraper.util.URLUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

public class IdealistaScrappingService
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaScrappingService.class);
    private static final String IDEALISTA_COM_EN = "https://www.idealista.com/en/";

    private WebDriverProvider webDriverProvider;
    private ExecutorService executor;
    private Properties props = PropertiesLoader.getProperties();

    public IdealistaScrappingService(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
        executor = ExecutorServiceProvider.getExecutor();
    }

    public Set<Advertisment> scrapSite(SearchAttribute searchAttribute) throws InterruptedException
    {

        int maxIterations = Integer.parseInt(props.getProperty("maxAdsToProcess", "100"));
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(IDEALISTA_COM_EN);
        Set<URL> categoriesUrls = getCategoriesUrls(searchAttribute);

        // Set<URL> categoriesUrls = FileUtils.readUrlsFromFile("cats_short.txt");

        Paginator paginator = new Paginator();
        Queue<URL> pagesToProcess = new ConcurrentLinkedQueue<>();

        categoriesUrls.forEach(e -> pagesToProcess.addAll(paginator.getAllPageUrls(driver, e.toString())));

        // pagesToProcess.forEach(LOGGER::info);

        Queue<Callable<Set<URL>>> tasks = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < maxIterations; i++)
        {
            if (!pagesToProcess.isEmpty())
            {
                URL page = pagesToProcess.poll();
                tasks.add(new SearchPageProcessor(webDriverProvider, page));
            }
        }
        LOGGER.info("INVOKING ..");
        List<Future<Set<URL>>> taskResults = executor.invokeAll(tasks);
        LOGGER.info("INVOKING FINISHED! ");

        Set<URL> adUrls = new HashSet<>();
        taskResults.forEach(e ->
        {
            try
            {
                adUrls.addAll(e.get());
            }
            catch (InterruptedException | ExecutionException e2)
            {
                e2.printStackTrace();
            }
        });

        Queue<Callable<Advertisment>> extractorTasks = new ConcurrentLinkedQueue<>();
        Iterator<URL> iterator = adUrls.iterator();
        for (int i = 0; i < maxIterations; i++)
        {
            if (iterator.hasNext())
            {
                URL page = iterator.next();
                AdvertismentExtractor extractor = new AdvertismentExtractor(webDriverProvider, page);
                extractor.setState(searchAttribute.getLocation());
                extractor.setType(RealtyType.fromString(searchAttribute.getTypology()));
                extractorTasks.add(extractor);
            }
        }
        List<Future<Advertisment>> ads = executor.invokeAll(extractorTasks);
        
        return ads.stream().map(e ->
        {
            try
            {
                return e.get();
            }
            catch (InterruptedException | ExecutionException e1)
            {
                e1.printStackTrace();
                return null;
            }
        }).collect(Collectors.toSet());

    }

    private Set<URL> getCategoriesUrls(SearchAttribute searchAttribute) throws InterruptedException
    {
        String operation = searchAttribute.getOperation();
        String typology = searchAttribute.getTypology();
        String location = searchAttribute.getLocation();
        StartPage startPage = new StartPage(webDriverProvider.get());

        Queue<Callable<String>> results = new ConcurrentLinkedQueue<>();

        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);
        if (StringUtils.isEmpty(operation))
        {
            LOGGER.info(
                    "Specified <operation> is null, will iterate through all available <operations + typology + locations>");
            // iterate ALL
            for (String chosenOperation : startPage.getAvailableOperations())
            {
                startPage.selectOperation(chosenOperation);
                for (String chosenTypology : startPage.getAvailableTypologies())
                {
                    startPage.selectTypology(chosenTypology);
                    for (String chosenLocation : startPage.getAvailableLocations())
                    {
                        results.add(chooser.new CategoryByOperationTypeLocation(
                                new SearchAttribute(chosenOperation, chosenTypology, chosenLocation),
                                webDriverProvider));
                    }
                }
            }
        }
        else
        {
            startPage.selectOperation(operation);
            if (StringUtils.isEmpty(typology))
            {
                LOGGER.info(
                        "Specified <typology> is null, will iterate through all available <typologies + locations> for specified <operation>",
                        operation);
                // iterate ALL typologies
                for (String chosenTypology : startPage.getAvailableTypologies())
                {
                    startPage.selectTypology(chosenTypology);
                    for (String chosenLocation : startPage.getAvailableLocations())
                    {
                        results.add(chooser.new CategoryByOperationTypeLocation(
                                new SearchAttribute(operation, chosenTypology, chosenLocation), webDriverProvider));
                    }
                }
            }
            else
            {
                startPage.selectTypology(typology);
                if (StringUtils.isEmpty(location))
                {
                    // iterate All locations
                    LOGGER.info(
                            "Will iterate through all available <locations> for specified <operation>: {} and <typology>: {}",
                            operation, typology);
                    for (String chosenLocation : startPage.getAvailableLocations())
                    {
                        results.add(chooser.new CategoryByOperationTypeLocation(
                                new SearchAttribute(operation, typology, chosenLocation), webDriverProvider));
                    }
                }
                else
                {
                    LOGGER.info("Will scrap only selected  <operation>: {}, <typology>: {} and <locations>: {}",
                            operation, typology, location);
                    startPage.selectLocation(location);
                    results.add(chooser.new CategoryByOperationTypeLocation(
                            new SearchAttribute(operation, typology, location), webDriverProvider));
                }

            }
        }
        List<Future<String>> categories = executor.invokeAll(results);
        Set<String> stringUrls = categories.stream().map(e ->
        {
            try
            {
                return e.get();
            }
            catch (InterruptedException | ExecutionException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
                return null;
            }
        }).collect(Collectors.toSet());
        return URLUtils.convertStringToUrls(stringUrls);
    }
}
