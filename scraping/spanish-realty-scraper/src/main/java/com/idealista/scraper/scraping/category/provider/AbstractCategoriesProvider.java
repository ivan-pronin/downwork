package com.idealista.scraper.scraping.category.provider;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Category;

public class AbstractCategoriesProvider
{
    private static final Logger LOGGER = LogManager.getLogger(AbstractCategoriesProvider.class);

    @Autowired
    private ExecutorServiceProvider executorServiceProvider;

    protected Set<Category> executeAndGetResults(Queue<Callable<Category>> results)
    {
        List<Future<Category>> categories = new ArrayList<>();
        try
        {
            categories = executorServiceProvider.getExecutor().invokeAll(results);
        }
        catch (InterruptedException e)
        {
            LOGGER.error("Error while executing tasks: {}", e.getMessage());
        }

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

    protected Set<Category> executeAndGetResultsForSet(Queue<Callable<Set<Category>>> results)
    {
        List<Future<Set<Category>>> categories = new ArrayList<>();
        try
        {
            categories = executorServiceProvider.getExecutor().invokeAll(results);
        }
        catch (InterruptedException e)
        {
            LOGGER.error("Error while executing tasks: {}", e.getMessage());
        }

        return categories.stream().map(t ->
        {
            try
            {
                return t.get();
            }
            catch (InterruptedException | ExecutionException e)
            {
                LOGGER.error("Error while retrieving category task result: {}", e);
            }
            return null;
        }).flatMap(Collection::stream).collect(Collectors.toSet());
    }
}
