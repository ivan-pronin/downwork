package com.idealista.scraper;

import java.io.IOException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.service.IdealistaScrappingService;
import com.idealista.scraper.util.PropertiesLoader;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.xls.TasksListener;
import com.idealista.scraper.xls.XlsExporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class RealtyApp
{
    private static final Logger LOGGER = LogManager.getLogger(RealtyApp.class);
    private static final Properties PROPERTIES = PropertiesLoader.getProperties();
    private static final String APP_VERSION = "rc-0.9";

    static
    {
        if (Boolean.getBoolean(PROPERTIES.getProperty("enableWebdriverLogging")))
        {
            System.setProperty("webdriver.chrome.logfile", "./chromedriver_" + getTimeStamp() + ".log");
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException
    {
        LOGGER.info("Program started");
        printProgramInfo(PROPERTIES);
        printEnvironmentInfo();
        Instant startTime = Instant.now();
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        String operation = PROPERTIES.getProperty("operation", null);
        String typology = PROPERTIES.getProperty("typology", null);
        String location = PROPERTIES.getProperty("location", null);

        BlockingQueue<Future<Advertisment>> advertismentExtractorTasks = new LinkedBlockingQueue<>();
        IdealistaScrappingService idealistaService = new IdealistaScrappingService(webDriverProvider,
                advertismentExtractorTasks);
        idealistaService.scrapSite(operation, typology, location);

        XlsExporter xlsExporter = new XlsExporter(PROPERTIES.getProperty("xlsFileName", "Scraper.xlsx"));
        TasksListener tasksListener = new TasksListener(advertismentExtractorTasks, xlsExporter);
        tasksListener.setAdUrlsInProgress(idealistaService.getAdvertismentUrlsInProgress());

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

    private static void printEnvironmentInfo()
    {
        LOGGER.info("");
        LOGGER.info("=== === Printing execution environment info  === ===");
        System.getenv().forEach((k, v) -> logEntry(k, v));
        System.getProperties().forEach((k, v) -> logEntry(k, v));
        LOGGER.info("=== === === === === === === === === === ===  === ===");
        LOGGER.info("");
    }

    private static void printProgramInfo(Properties props)
    {
        LOGGER.info("=== === Printing program info   === ===");
        LOGGER.info("");
        LOGGER.info("App version: {}", APP_VERSION);
        LOGGER.info("");
        LOGGER.info("... ... Printing App properties ... ... ");
        props.forEach((k, v) -> logEntry(k, v));
        LOGGER.info("... ... ... ... ... ... ... ... ... ... ");
    }

    private static void printExecutionTime(Instant startTime)
    {
        Duration d = Duration.between(startTime, Instant.now());
        LOGGER.info("Total time taken: {} hrs {} mins {} secs", d.toHours(), d.toMinutes() % 60, d.getSeconds() % 60);
    }

    private static void logEntry(Object k, Object v)
    {
        LOGGER.info("{} = {}", k, v);
    }

    private static String getTimeStamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH-mm-ss_SSS");
        return sdf.format(new Timestamp(new java.util.Date().getTime()));
    }
}
