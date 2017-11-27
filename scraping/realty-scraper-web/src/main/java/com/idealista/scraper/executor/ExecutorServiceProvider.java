package com.idealista.scraper.executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.web.config.BaseScraperConfiguration;

@Component
public final class ExecutorServiceProvider
{
    private static ExecutorService instance;

    @Autowired
    private BaseScraperConfiguration scraperConfiguration;

    public ExecutorService getExecutor()
    {
        if (instance == null || instance.isTerminated())
        {
            instance = Executors.newFixedThreadPool(scraperConfiguration.getMaxThreads());
        }
        return instance;
    }
}
