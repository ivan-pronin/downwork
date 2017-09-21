package com.idealista.scraper.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public final class ExecutorServiceProvider
{
    private static ExecutorService instance;

    @Value("${maxThreads}")
    private int maxThreads;

    public ExecutorService getExecutor()
    {
        if (instance == null)
        {
            instance = Executors.newFixedThreadPool(maxThreads);
        }
        return instance;
    }
}
