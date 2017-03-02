package com.idealista.scraper.xls;

import java.net.URL;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

import com.idealista.scraper.data.DataSource;
import com.idealista.scraper.data.IDataSource;
import com.idealista.scraper.model.Advertisment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class TasksListener extends TimerTask
{
    private static final Logger LOGGER = LogManager.getLogger(TasksListener.class);
    private BlockingQueue<Future<Advertisment>> advertismentExtractorResults;
    private BlockingQueue<URL> adUrlsInProgress;
    private IDataSource dataSource = new DataSource();
    private int processedUrls;
    private int failedUrls;
    XlsExporter xlsExporter;

    public TasksListener(BlockingQueue<Future<Advertisment>> advertismentExtractorResults, XlsExporter xlsExporter)
    {
        this.advertismentExtractorResults = advertismentExtractorResults;
        this.xlsExporter = xlsExporter;
    }

    @Override
    public void run()
    {
        LOGGER.info("Polling the queue for newly scrapped advertisments ...");
        Iterator<Future<Advertisment>> iterator = advertismentExtractorResults.iterator();
        Set<Advertisment> advertisments = new HashSet<>();
        while (iterator.hasNext())
        {
            Future<Advertisment> task = iterator.next();
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
            dataSource.updateProcessedUrls("./data/processedAds.txt",
                    advertisments.stream().map(e -> e.getUrl()).collect(Collectors.toSet()));
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
}
