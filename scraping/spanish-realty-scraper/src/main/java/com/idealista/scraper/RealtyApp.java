package com.idealista.scraper;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.search.SearchAttribute;
import com.idealista.scraper.service.IdealistaScrappingService;
import com.idealista.scraper.util.LoggingUtils;
import com.idealista.scraper.util.PropertiesLoader;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.xls.XlsExporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Properties;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RealtyApp
{
    private static final Logger LOGGER = LogManager.getLogger(RealtyApp.class);

    private static final int MAX_ITERATIONS = 10;

    static
    {
        LoggingUtils.turnOffHtmlUnitWarnings();
    }

    public static void main(String[] args) throws InterruptedException, IOException
    {
        // open main page url
        // perform selection of necessary categories, search for results
        // results appeared - open them
        // calculate total number of pages
        // implement mechanism to generate each page url based on total pages
        // process each results page and grab advertisments URLs - collect all URLs into single collection
        // exclude unnecessary ads URLs from parsing
        //

        String baseUrl = "https://www.idealista.com/en/";
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(baseUrl);

        Properties props = PropertiesLoader.getProperties();
        String operation = props.getProperty("operation", null);
        String typology = props.getProperty("typology", null);
        String location = props.getProperty("location", null);
        SearchAttribute searchAttribute = new SearchAttribute(operation, typology, location);

        IdealistaScrappingService idealistaService = new IdealistaScrappingService(webDriverProvider);
        Set<Advertisment> ads = idealistaService.scrapSite(searchAttribute);
        ExecutorServiceProvider.getExecutor().shutdown();
        XlsExporter exporter = new XlsExporter();
        exporter.exportToXls(ads, props.getProperty("xlsFileName", "Scraper.xlsx"));
        webDriverProvider.destroy();
    }

    private static URL getRandomPage(Queue<URL> pagesToProcess)
    {
        Random rand = new Random();
        int randomInt = rand.nextInt(100);
        return (URL) pagesToProcess.toArray()[randomInt];
    }

    private static void printExecutionTime(String message, long startTime)
    {
        long endTime = System.currentTimeMillis();
        int seconds = (int) ((endTime - startTime) / 1000);
        LOGGER.info("{}. Total time taken: {} seconds", message, seconds);
    }

    private static <T> Set<T> newConcurrentHashSet()
    {
        return Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
    }
}
