package com.idealista.scraper.scraping.category;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.scraper.data.IDataSource;
import com.idealista.scraper.data.IDataTypeService;
import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.scraping.searchpage.processor.ISeachPageProcessor;
import com.idealista.scraper.util.URLUtils;

@Component
public class AdUrlsFinder implements IAdUrlsFinder
{
    private static final Logger LOGGER = LogManager.getLogger(AdUrlsFinder.class);
    private static int totalNewUrlsCounter;

    private Queue<Category> searchPagesToProcess = new ConcurrentLinkedQueue<>();
    private Set<Long> processedIds = new HashSet<>();
    private Set<Category> categoriesBaseUrls;
    private Set<URL> processedUrls;

    private Set<URL> newUrls;

    @Autowired
    private ExecutorServiceProvider executorProvider;

    @Autowired
    private IDataSource dataSource;

    @Autowired
    private IPaginator paginator;

    @Autowired
    private Supplier<ISeachPageProcessor> searchPageProcessorSupplier;

    @Autowired
    private IDataTypeService dataTypeService;

    @Override
    public Set<Category> findNewAdUrlsAmount(int neededAmount)
    {
        categoriesBaseUrls.forEach(e -> searchPagesToProcess.addAll(paginator.getAllPageUrls(e)));
        Set<Category> adUrlsToProcess = newConcurrentHashSet();

        String newUrlsFileName = dataTypeService.getNewAdsFileName();
        newUrls = dataSource.getUrlsFromFile(newUrlsFileName);

        processedUrls = dataSource.getUrlsFromFile(dataTypeService.getProcessedAdsFileName());
        processedUrls.forEach(e -> processedIds.add(URLUtils.extractIdFromUrl(e)));

        int diff = newUrls.size() - neededAmount;
        Category templateCategory = categoriesBaseUrls.isEmpty() ? new Category()
                : categoriesBaseUrls.iterator().next();
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
            while (!searchPagesToProcess.isEmpty() && adUrlsToProcess.size() < neededAmount)
            {
                adUrlsToProcess.addAll(grabNextAdUrls());
            }
        }
        LOGGER.info("Total new advertisement URLS to be processed in current session: " + adUrlsToProcess.size());
        dataSource.writeUrlsToFile(newUrlsFileName,
                adUrlsToProcess.stream().map(Category::getUrl).collect(Collectors.toSet()));
        return adUrlsToProcess;
    }

    public void setCategoriesBaseUrls(Set<Category> categoriesBaseUrls)
    {
        this.categoriesBaseUrls = categoriesBaseUrls;
    }

    public void setDataSource(IDataSource dataSource)
    {
        this.dataSource = dataSource;
    }

    public void setExecutor(ExecutorServiceProvider executorProvider)
    {
        this.executorProvider = executorProvider;
    }

    public void setPaginator(IPaginator paginator)
    {
        this.paginator = paginator;
    }

    private static <T> Set<T> newConcurrentHashSet()
    {
        return Collections.newSetFromMap(new ConcurrentHashMap<T, Boolean>());
    }

    private Set<Category> grabNextAdUrls()
    {
        Set<Category> foundUrls = newConcurrentHashSet();
        Queue<Callable<Set<Category>>> tasks = new ConcurrentLinkedQueue<>();
        if (!searchPagesToProcess.isEmpty())
        {
            ISeachPageProcessor seachPageProcessor = searchPageProcessorSupplier.get();
            seachPageProcessor.setPage(searchPagesToProcess.poll());
            tasks.add(seachPageProcessor);
        }
        List<Future<Set<Category>>> taskResults = new ArrayList<>();
        try
        {
            taskResults = executorProvider.getExecutor().invokeAll(tasks);
        }
        catch (InterruptedException e1)
        {
            LOGGER.error("Error while executing ISearchPageProcessor task: {}", e1);
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
        Set<Category> newUniqueUrls = foundUrls.stream()
                .filter(e -> !processedIds.contains(URLUtils.extractIdFromUrl(e.getUrl()))).collect(Collectors.toSet());
        int newUrlsCount = newUniqueUrls.size();
        totalNewUrlsCounter += newUrlsCount;
        LOGGER.info("Added new advertisment urls: {}, total new ads count: {}", newUrlsCount, totalNewUrlsCounter);
        return newUniqueUrls;
    }
}
