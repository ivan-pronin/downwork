package com.idealista.scraper.search;

import com.idealista.scraper.page.StartPage;
import com.idealista.scraper.page.StartPageTests;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class CategoriesChooserTests
{
    private static final String IDEALISTA_COM_EN = "https://www.idealista.com/en/";
    private static final Logger LOGGER = LogManager.getLogger(StartPageTests.class);
    private WebDriverProvider webDriverProvider = new WebDriverProvider();

    // @Test
    public void testGetAllCategoriesUrls()
    {
        webDriverProvider.get().navigate().to(IDEALISTA_COM_EN);
        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);
        chooser.getAllCategoriesUrls();
    }

    // @Test
    public void testGetCatsByOperationAndTypology()
    {
        webDriverProvider.get().navigate().to(IDEALISTA_COM_EN);
        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);
        Set<String> result = chooser.getCategoriesUrlsByOperationAndTypology("Share", "Homes");
        LOGGER.info("Total cats for Share + Homes: {} LIST: {}", result.size(), result);
    }

    // @Test
    public void getGetCategoriesWithoutShowAllLink()
    {
        webDriverProvider.get().navigate().to(IDEALISTA_COM_EN);
        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);
        Set<String> result = chooser.getCategoryUrlByOperationTypologyAndLocation("Share", "Homes", "Fuerteventura");
        LOGGER.info("REsult: {}", result);
    }

    @Test
    public void testCallable() throws InterruptedException
    {
        webDriverProvider.get().navigate().to(IDEALISTA_COM_EN);
        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);
        StartPage startPage = new StartPage(webDriverProvider.get());
        startPage.selectOperation("Share");
        startPage.selectTypology("Homes");
        Set<String> locations = startPage.getAvailableLocations();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        Queue<Future<String>> results = new ConcurrentLinkedQueue<>();
        for (String location : locations)
        {
            results.add(executor.submit(chooser.new CategoryByOperationTypeLocation(
                    new SearchAttribute("Share", "Homes", location), webDriverProvider)));
        }
        executor.shutdown();
        executor.awaitTermination(600, TimeUnit.SECONDS);
        LOGGER.info(" >>>>>>>>>>>  All tasks seem to be finished!");
        LOGGER.info(" >>>>>>>>>>>  Printing results...");
        LOGGER.info("Results size: {}", results.size());
        results.forEach(e -> {
            try
            {
                LOGGER.info(e.get());
            }
            catch (InterruptedException | ExecutionException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
    }
}
