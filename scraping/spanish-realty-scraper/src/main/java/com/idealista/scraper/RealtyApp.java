package com.idealista.scraper;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.search.SearchAttribute;
import com.idealista.scraper.service.IdealistaScrappingService;
import com.idealista.scraper.util.PropertiesLoader;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.xls.TasksListener;
import com.idealista.scraper.xls.XlsExporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class RealtyApp
{
    private static final Logger LOGGER = LogManager.getLogger(RealtyApp.class);

    public static void main(String[] args) throws InterruptedException, IOException
    {
        LOGGER.info("Program started");
        Instant startTime = Instant.now();
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        Properties props = PropertiesLoader.getProperties();
        String operation = props.getProperty("operation", null);
        String typology = props.getProperty("typology", null);
        String location = props.getProperty("location", null);
        SearchAttribute searchAttribute = new SearchAttribute(operation, typology, location);

        BlockingQueue<Future<Advertisment>> advertismentExtractorTasks = new LinkedBlockingQueue<>();
        IdealistaScrappingService idealistaService = new IdealistaScrappingService(webDriverProvider,
                advertismentExtractorTasks);
        idealistaService.scrapSite(searchAttribute);

        XlsExporter xlsExporter = new XlsExporter(props.getProperty("xlsFileName", "Scraper.xlsx"));
        TasksListener tasksListener = new TasksListener(advertismentExtractorTasks, xlsExporter);
        Timer timer = new Timer();
        ExecutorService executor = ExecutorServiceProvider.getExecutor();
        executor.shutdown();
        timer.schedule(tasksListener, 0, 30 * 1000);

        while (!executor.isTerminated() && !advertismentExtractorTasks.isEmpty())
        {
            LOGGER.info("Waiting for remaining tasks to be finished...");
            long start = System.currentTimeMillis();
            while (System.currentTimeMillis() - start < 60000)
                ;
        }
        if (executor.isTerminated())
        {
            LOGGER.info("Executor has been terminated (all ads has been processed)");
            LOGGER.info("Tasks list size: {}", advertismentExtractorTasks.size());
            tasksListener.run();
        }
        LOGGER.info("Terminating TasksListener...");
        timer.cancel();
        webDriverProvider.destroy();
        LOGGER.info("Program has finished working successfully!");
        printExecutionTime(startTime);
    }

    private static void printExecutionTime(Instant startTime)
    {
        Duration d = Duration.between(startTime, Instant.now());
        LOGGER.info("Total time taken: {} hrs {} mins {} secs", d.toHours(), d.toMinutes() % 60, d.getSeconds() % 60);
    }
}
