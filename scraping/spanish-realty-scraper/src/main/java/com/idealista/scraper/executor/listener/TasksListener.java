package com.idealista.scraper.executor.listener;

import com.idealista.scraper.data.IDataSource;
import com.idealista.scraper.data.IDataTypeService;
import com.idealista.scraper.data.xls.XlsExporter;
import com.idealista.scraper.model.Advertisement;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

@Component
public class TasksListener extends TimerTask
{
    private static final Logger LOGGER = LogManager.getLogger(TasksListener.class);
    private BlockingQueue<Future<Advertisement>> advertismentExtractorResults;
    private BlockingQueue<URL> adUrlsInProgress;

    @Autowired
    private IDataSource dataSource;

    @Autowired
    private XlsExporter xlsExporter;
    
    @Autowired
    private IDataTypeService dataTypeService;

    private int processedUrls;
    private int failedUrls;

    @Override
    public void run()
    {
        LOGGER.info("Polling the queue for newly scrapped advertisments ...");
        Iterator<Future<Advertisement>> iterator = advertismentExtractorResults.iterator();
        Set<Advertisement> advertisments = new HashSet<>();
        while (iterator.hasNext())
        {
            Future<Advertisement> task = iterator.next();
            if (task.isDone())
            {
                try
                {
                    advertisments.add(task.get());
                }
                catch (InterruptedException | ExecutionException e)
                {
                    LOGGER.error("Error while adding advertisment to results: {}", e);
                    failedUrls++;
                }
                iterator.remove();
                processedUrls++;
            }
        }
        if (!advertisments.isEmpty())
        {
            LOGGER.info("Prepared <{}> results for exporting", advertisments.size());
            xlsExporter.appendResults(advertisments);
            Set<URL> urls = advertisments.stream().map(e -> e.getUrl()).collect(Collectors.toSet());
            dataSource.writeUrlsToFile(dataTypeService.getProcessedAdsFileName(), urls);
            dataSource.removeUrlsFromFile(dataTypeService.getNewAdsFileName(), urls);
        }
        LOGGER.info(
                "AD urls in progress: {} | Processed urls so far: {} | Remaining URLs to process: {} | Failed urls: {}",
                adUrlsInProgress.size(), processedUrls, adUrlsInProgress.size() - processedUrls - failedUrls,
                failedUrls);
    }

    public void setAdUrlsInProgress(BlockingQueue<URL> adUrlsInProgress)
    {
        this.adUrlsInProgress = adUrlsInProgress;
    }

    public void setAdvertismentExtractorResults(BlockingQueue<Future<Advertisement>> advertismentExtractorResults)
    {
        this.advertismentExtractorResults = advertismentExtractorResults;
    }
}
