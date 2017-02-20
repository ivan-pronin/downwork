package com.idealista.scraper.executor;

import com.idealista.scraper.util.PropertiesLoader;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ExecutorServiceProvider
{
    private static ExecutorService instance;

    public static ExecutorService getExecutor()
    {
        if (instance == null)
        {
            instance = Executors.newFixedThreadPool(
                    Integer.parseInt(PropertiesLoader.getProperties().getProperty("maxThreads", "2")));
        }
        return instance;
    }
}
