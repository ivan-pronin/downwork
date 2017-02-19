package com.idealista.scraper;

import com.idealista.scraper.model.RequestCounter;
import com.idealista.scraper.page.Paginator;
import com.idealista.scraper.page.SearchPageProcessor;
import com.idealista.scraper.task.TasksHandler;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.FileNotFoundException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Queue;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class IpBlockingTests
{
    private static final Logger LOGGER = LogManager.getLogger(IpBlockingTests.class);
    private static final int MAX_SEARCH_PAGE_ITERATIONS = 9999;
    private static final int MAX_ADS_URLS_ITERATIONS = 999999;

    private volatile int counter = 0;

    @Test
    public void testCountRequestsForIPBlocking()
    {
        // String baseUrl = "https://www.idealista.com/venta-viviendas/segovia-provincia/";
        // String baseUrl = "https://www.idealista.com/venta-viviendas/salamanca-provincia/";
        //String baseUrl = "https://www.idealista.com/venta-viviendas/almeria-provincia/";
        String baseUrl = "https://www.idealista.com/venta-viviendas/barcelona-provincia/";
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(baseUrl);

        Paginator paginator = new Paginator();
        Queue<URL> pagesToProcess = new ConcurrentLinkedQueue<>();
        pagesToProcess.addAll(paginator.getAllPageUrls(driver, baseUrl));

        ExecutorService executor = Executors.newFixedThreadPool(8);

        Queue<Future<Set<URL>>> adsResults = new ConcurrentLinkedQueue<>();
        Set<URL> adsUrls = newConcurrentHashSet();

        TasksHandler tasksHandler = new TasksHandler();
        tasksHandler.setTaskCompletionTimeout(30);
        tasksHandler.setTaskCompletionTimeUnit(TimeUnit.SECONDS);

        for (int i = 0; i < MAX_SEARCH_PAGE_ITERATIONS; i++)
        {
            adsResults.add(executor.submit(new SearchPageProcessor(webDriverProvider, pagesToProcess.poll())));
        }
        LOGGER.info("Begin waiting for search page iterations to complete ... ");
        tasksHandler.waitTasksForCompletion(adsResults);
        LOGGER.info("ENDED: waiting for search page iterations to complete");

        adsResults.forEach(e ->
        {
            try
            {
                adsUrls.addAll(e.get());
            }
            catch (InterruptedException | ExecutionException e3)
            {
                // TODO Auto-generated catch block
                e3.printStackTrace();
            }
        });

        LOGGER.info("Total ads URL were found: {}", adsUrls.size());
        LOGGER.info("Printing search pages found ...");
        System.out.println("=== === ===");
        adsUrls.forEach(LOGGER::info);
        System.out.println("=== === ===");
/*
        Queue<Future<Integer>> results = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < MAX_ADS_URLS_ITERATIONS; i++)
        {
            adsUrls.forEach(e -> results.add(executor.submit(new RequestCounter(webDriverProvider, e))));
        }

        LOGGER.info("Start waiting for RESULTS extracting from Ads pages");
        tasksHandler.waitTasksForCompletion(results);
        LOGGER.info("ENDED: waiting for RESULTS extracting from Ads pages");

       

        try
        {
            executor.awaitTermination(60, TimeUnit.SECONDS);
        }
        catch (InterruptedException e2)
        {
            e2.printStackTrace();
        }

        results.forEach(e ->
        {
            try
            {
                LOGGER.info(e.get());
            }
            catch (InterruptedException e1)
            {
                e1.printStackTrace();
            }
            catch (ExecutionException e1)
            {
                e1.printStackTrace();
            }
        });*/
        
        executor.shutdown();
        webDriverProvider.destroy();
    }

    private static <T> Set<T> newConcurrentHashSet()
    {
        return Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
    }

    // @Test
    public void testStartMultithreadedBrowsers() throws Exception
    {
        ExecutorService executor = Executors.newFixedThreadPool(5);
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        int instances = 30;

        for (int i = 0; i < instances; i++)
        {
            executor.submit(() ->
            {
                WebDriver driver = webDriverProvider.get();
                counter++;
                driver.navigate().to("http://www.onliner.by");
                LOGGER.info("Window handle: {}", driver.getWindowHandle());
                webDriverProvider.end();
            });
        }
        // executor.shutdown();
        executor.awaitTermination(60, TimeUnit.SECONDS);
        LOGGER.info("Counter on finish: {}", counter);
    }

    // @Test
    public void testReadFile() throws Exception
    {
        Set<URL> urls = readUrlsFromFile();
        System.out.println("Total URLS were read from file: " + urls.size());
    }

    //@Test
    public void testExtractAdsFromFile()
    {
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        
        ExecutorService executor = Executors.newFixedThreadPool(8);

        Set<URL> adsUrls = readUrlsFromFile();

        TasksHandler tasksHandler = new TasksHandler();
        tasksHandler.setTaskCompletionTimeout(30);
        tasksHandler.setTaskCompletionTimeUnit(TimeUnit.SECONDS);

        LOGGER.info("Total ads URL were found: {}", adsUrls.size());

        Queue<Future<Integer>> results = new ConcurrentLinkedQueue<>();
        Iterator<URL> iterator = adsUrls.iterator();
        for (int i = 0; i < MAX_ADS_URLS_ITERATIONS; i++)
        {
            while (iterator.hasNext())
            {
                URL url = iterator.next();
                results.add(executor.submit(new RequestCounter(webDriverProvider, url)));
            }
        }

        LOGGER.info("Start waiting for RESULTS extracting from Ads pages");
        tasksHandler.waitTasksForCompletion(results);
        LOGGER.info("ENDED: waiting for RESULTS extracting from Ads pages");

        executor.shutdown();
    }
    private Set<URL> readUrlsFromFile()
    {
        Scanner sc = null;
        try
        {
            sc = new Scanner(new File("urls.txt"));
        }
        catch (FileNotFoundException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        Set<String> urlsString = new HashSet<>();
        String separator = "https://";
        while (sc.hasNextLine())
        {
            String line = sc.nextLine();
            if (line.contains(separator))
            {
                urlsString.add(line.split(separator)[1]);
            }
            continue;
        }
        return urlsString.stream().map(t ->
        {
            try
            {
                return new URL(separator + t);
            }
            catch (MalformedURLException e)
            {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet());
    }
}
