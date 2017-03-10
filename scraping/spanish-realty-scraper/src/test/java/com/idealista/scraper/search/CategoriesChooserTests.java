package com.idealista.scraper.search;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.page.StartPageTests;
import com.idealista.scraper.scraping.search.CategoriesChooser;
import com.idealista.scraper.ui.page.StartPage;
import com.idealista.scraper.util.PropertiesLoader;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.Properties;
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

    // @Test
    public void testGetAllCategoriesUrls()
    {
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        webDriverProvider.get().navigate().to(IDEALISTA_COM_EN);
        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);
        chooser.getAllCategoriesUrls();
    }

    // @Test
    public void testGetCatsByOperationAndTypology()
    {
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        webDriverProvider.get().navigate().to(IDEALISTA_COM_EN);
        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);
        Set<String> result = chooser.getCategoriesUrlsByOperationAndTypology("Share", "Homes");
        LOGGER.info("Total cats for Share + Homes: {} LIST: {}", result.size(), result);
    }

    // @Test
    public void getGetCategoriesWithoutShowAllLink()
    {
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        webDriverProvider.get().navigate().to(IDEALISTA_COM_EN);
        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);
        Set<String> result = chooser.getCategoryUrlByOperationTypologyAndLocation("Share", "Homes", "Fuerteventura");
        LOGGER.info("REsult: {}", result);
    }

    // @Test
    public void testCallable() throws InterruptedException
    {
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        webDriverProvider.get().navigate().to(IDEALISTA_COM_EN);
        CategoriesChooser chooser = new CategoriesChooser(webDriverProvider);
        StartPage startPage = new StartPage(webDriverProvider.get());
        startPage.selectOperation("Share");
        startPage.selectTypology("Homes");
        Set<String> locations = startPage.getAvailableLocations();
        ExecutorService executor = Executors.newFixedThreadPool(8);
        Queue<Future<Category>> results = new ConcurrentLinkedQueue<>();
        for (String location : locations)
        {
            results.add(executor.submit(chooser.new CategoryBySearchAndFilterAttributes("Share", "Homes", location)));
        }
        executor.shutdown();
        executor.awaitTermination(600, TimeUnit.SECONDS);
        LOGGER.info(" >>>>>>>>>>>  All tasks seem to be finished!");
        LOGGER.info(" >>>>>>>>>>>  Printing results...");
        LOGGER.info("Results size: {}", results.size());
        results.forEach(e ->
        {
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

    //@Test
    public void testParsingCategories() throws Exception
    {
        //String operation = "Buy";
        //String operation = null;
        String operation = "Buy,Share";
//        String typology = "Homes";
        String typology = null;
//        String typology = "Homes,Buildings";
        String location = null;

        WebDriver driver = new ChromeDriver();
        driver.navigate().to(IDEALISTA_COM_EN);
        StartPage page = new StartPage(driver);
        System.out.println(page.getSearchAttributes(operation, typology, location));
    }
    @Test
    public void testParsingProps() throws Exception
    {
        Properties PROPERTIES = PropertiesLoader.getProperties();
        String operation = PROPERTIES.getProperty("operation", null);
        String typology = PROPERTIES.getProperty("typology", null);
        String location = PROPERTIES.getProperty("location", null);
        System.out.println("Operation: " + operation + ", typology: " + typology+ ", location: "+location);
    }
}
