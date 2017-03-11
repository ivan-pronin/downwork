package com.idealista.scraper.scraping.search;

import com.idealista.scraper.data.DataType;
import com.idealista.scraper.data.IDataSource;
import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.ISearchPageProcessorFactory;
import com.idealista.scraper.scraping.Paginator;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class AdUrlsFinder implements IAdUrlsFinder
{
    private static final Logger LOGGER = LogManager.getLogger(AdUrlsFinder.class);
    private static int totalNewUrlsCounter;

    private Queue<Category> searchPagesToProcess = new ConcurrentLinkedQueue<>();
    private Set<Category> categoriesBaseUrls;
    private Set<URL> processedUrls;
    private Set<URL> newUrls;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private ExecutorServiceProvider executorProvider;

    @Autowired
    private IDataSource dataSource;

    @Autowired
    private Paginator paginator;

    @Autowired
    private ISearchPageProcessorFactory searchPageProcessorFactory;

    @Override
    public Set<Category> findNewAdUrlsAmount(int neededAmount)
    {
        categoriesBaseUrls
                .forEach(e -> searchPagesToProcess.addAll(paginator.getAllPageUrls(webDriverProvider.get(), e)));
        Set<Category> adUrlsToProcess = newConcurrentHashSet();
        newUrls = dataSource.getUrlsFromFile(DataType.NEW_ADS);
        processedUrls = dataSource.getUrlsFromFile(DataType.PROCESSED_ADS);

        int diff = newUrls.size() - neededAmount;
        Category templateCategory = categoriesBaseUrls.iterator().next();
        if (diff >= 0)
        {
            Iterator<URL> iterator = newUrls.iterator();
            while (diff > 0)
            {
                iterator.next();
                iterator.remove();
                diff--;
            }
            newUrls.forEach(e -> adUrlsToProcess.add(new Category(e, templateCategory)));
        }
        else
        {
            newUrls.forEach(e -> adUrlsToProcess.add(new Category(e, templateCategory)));
            while (!searchPagesToProcess.isEmpty())
            {
                if (adUrlsToProcess.size() >= neededAmount)
                {
                    break;
                }
                adUrlsToProcess.addAll(grabNextAdUrls());
            }
        }
        LOGGER.info("Total new advertisement URLS to be processed in current session: " + adUrlsToProcess.size());
        dataSource.writeUrlsToFile(DataType.NEW_ADS,
                adUrlsToProcess.stream().map(Category::getUrl).collect(Collectors.toSet()));
        return adUrlsToProcess;
    }

    private Set<Category> grabNextAdUrls()
    {
        Set<Category> foundUrls = newConcurrentHashSet();
        Queue<Callable<Set<Category>>> tasks = new ConcurrentLinkedQueue<>();
        if (!searchPagesToProcess.isEmpty())
        {
            Category page = searchPagesToProcess.poll();
            tasks.add(searchPageProcessorFactory.create(page));
        }
        List<Future<Set<Category>>> taskResults = new ArrayList<>();
        try
        {
            taskResults = executorProvider.getExecutor().invokeAll(tasks);
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
                LOGGER.error("Error while retrieving ad url from category task: {}", e2);
            }
        });
        Set<Category> newUniqueUrls = foundUrls.stream().filter(e -> !processedUrls.contains(e.getUrl()))
                .collect(Collectors.toSet());
        int newUrlsCount = newUniqueUrls.size();
        totalNewUrlsCounter += newUrlsCount;
        LOGGER.info("Added new advertisment urls: {}, total new ads count: {}", newUrlsCount, totalNewUrlsCounter);
        return newUniqueUrls;
    }

    private static <T> Set<T> newConcurrentHashSet()
    {
        return Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
    }

    public void setCategoriesBaseUrls(Set<Category> categoriesBaseUrls)
    {
        this.categoriesBaseUrls = categoriesBaseUrls;
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }

    public void setExecutor(ExecutorServiceProvider executorProvider)
    {
        this.executorProvider = executorProvider;
    }

    public void setDataSource(IDataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public void setPaginator(Paginator paginator)
    {
        this.paginator = paginator;
    }

    public void setSearchPageProcessorFactory(ISearchPageProcessorFactory searchPageProcessorFactory)
    {
        this.searchPageProcessorFactory = searchPageProcessorFactory;
    }
}
