package com.idealista.scraper.service;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.idealista.scraper.data.DataSource;
import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.page.Paginator;
import com.idealista.scraper.page.SearchPageProcessor;
import com.idealista.scraper.search.Category;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdUrlsFinder implements IAdUrlsFinder
{
    private static final Logger LOGGER = LogManager.getLogger(AdUrlsFinder.class);

    private Queue<Category> searchPagesToProcess = new ConcurrentLinkedQueue<>();
    private Set<Category> categoriesBaseUrls;
    private Set<URL> processedUrls;
    private WebDriverProvider webDriverProvider;
    private ExecutorService executor;

    public AdUrlsFinder(WebDriverProvider webDriverProvider, Set<Category> categoriesBaseUrls)
    {
        this.webDriverProvider = webDriverProvider;
        this.categoriesBaseUrls = categoriesBaseUrls;
        executor = ExecutorServiceProvider.getExecutor();
    }

    @Override
    public Set<Category> findNewAdUrlsAmount(int neededAmount)
    {
        Paginator paginator = new Paginator();
        categoriesBaseUrls
                .forEach(e -> searchPagesToProcess.addAll(paginator.getAllPageUrls(webDriverProvider.get(), e)));
        Set<Category> adUrlsToProcess = newConcurrentHashSet();
        
        DataSource dataSource = new DataSource();
        processedUrls = dataSource.getProcessedUrls("./data/processedAds.txt");
        
        while (!searchPagesToProcess.isEmpty())
        {
            if (adUrlsToProcess.size() >= neededAmount)
            {
                break;
            }
            adUrlsToProcess.addAll(grabNextAdUrls());
        }
        LOGGER.info("Total filtered ads: "+adUrlsToProcess.size());
        return adUrlsToProcess;
    }

    private Set<Category> grabNextAdUrls()
    {
        Set<Category> foundUrls = newConcurrentHashSet();
        Queue<Callable<Set<Category>>> tasks = new ConcurrentLinkedQueue<>();
        for (int i = 0; i < 10 ; i++)
        {
            if (!searchPagesToProcess.isEmpty())
            {
                Category page = searchPagesToProcess.poll();
                tasks.add(new SearchPageProcessor(webDriverProvider, page));
            }
        }
        List<Future<Set<Category>>> taskResults = new ArrayList<>();
        try
        {
            taskResults = executor.invokeAll(tasks);
        }
        catch (InterruptedException e1)
        {
            LOGGER.error("Error while executing SearchPageProcessor task: {}", e1);
        }
        
        taskResults.forEach(e ->
        {
            try
            {
                foundUrls.addAll(e.get());
            }
            catch (InterruptedException | ExecutionException e2)
            {
                LOGGER.error("Error while retrieving ad url from category task: {}", e);
            }
        });
        return foundUrls.stream().filter(e -> !processedUrls.contains(e.getUrl())).collect(Collectors.toSet());
    }

    private static <T> Set<T> newConcurrentHashSet()
    {
        return Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
    }
}
