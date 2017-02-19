package com.idealista.scraper.xls;

import com.idealista.scraper.model.Advertisment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.TimerTask;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class TasksListener extends TimerTask
{
    private static final Logger LOGGER = LogManager.getLogger(TasksListener.class);
    private BlockingQueue<Future<Advertisment>> advertismentExtractorTasks;
    XlsExporter xlsExporter;

    public TasksListener(BlockingQueue<Future<Advertisment>> advertismentExtractorTasks, XlsExporter xlsExporter)
    {
        this.advertismentExtractorTasks = advertismentExtractorTasks;
        this.xlsExporter = xlsExporter;
    }

    @Override
    public void run()
    {
        LOGGER.info("Polling the queue for newly scrapped advertisments ...");
        Iterator<Future<Advertisment>> iterator = advertismentExtractorTasks.iterator();
        Set<Advertisment> advertisments = new HashSet<>();
        while (iterator.hasNext())
        {
            Future<Advertisment> task = iterator.next();
            if (task.isDone())
            {
                iterator.remove();
                try
                {
                    advertisments.add(task.get());
                }
                catch (InterruptedException | ExecutionException e)
                {
                    LOGGER.error("Error while adding advertisment to results: {}", e);
                }
            }
        }
        if (!advertisments.isEmpty())
        {
            LOGGER.info("Prepared <{}> results for exporting", advertisments.size());
            xlsExporter.appendResults(advertisments);
        }
    }
}
