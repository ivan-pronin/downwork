package com.idealista.scraper;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

import com.idealista.scraper.data.xls.XlsExporter;
import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.executor.listener.TasksListener;
import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.LaunchResult;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.util.DateTimeUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.web.config.BaseScraperConfiguration;
import com.idealista.web.config.BaseSourceConfiguration;

@Component
public class RealtyApp
{
    private static final Logger LOGGER = LogManager.getLogger(RealtyApp.class);
    private static final String APP_VERSION = "1.4.1";

    @Autowired
    private ApplicationContext appContext;

    @Value("${enableWebdriverLogging}")
    private boolean enableWebdriverLogging;

    @Value("settings/sources/${scrapTarget}.properties")
    private String sourceProperties;

    private TasksListener tasksListener;

    @Autowired
    private XlsExporter xlsExporter;

    @Autowired
    private ExecutorServiceProvider executorService;

    @Autowired
    private IScrappingService scrappingService;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private BaseScraperConfiguration scraperConfiguration;

    @Autowired
    private BaseSourceConfiguration sourceConfiguration;

    @Autowired
    private LaunchResult launchResult;

    @PostConstruct
    private void initSystemProperties()
    {
        if (enableWebdriverLogging)
        {
            System.setProperty("webdriver.chrome.logfile",
                    "./logs/chromedriver_" + DateTimeUtils.getTimestamp() + ".log");
        }
    }

    public void printInfo() throws IOException
    {
        LOGGER.info("Program started");
        Instant startTime = Instant.now();
        launchResult.setStartTime(startTime);
        printProgramInfo();
        printEnvironmentInfo();
    }

    public void printBootContextData()
    {
        LOGGER.info("PRINTING ScraperConfig: " + scraperConfiguration);
        LOGGER.info("PRINTING SourceConfig: " + sourceConfiguration);
    }

    public void run() throws InterruptedException, IOException
    {
        xlsExporter.initWorkBook();
        tasksListener = appContext.getBean(TasksListener.class);
        BlockingQueue<Future<Advertisement>> advertismentExtractorTasks = new LinkedBlockingQueue<>();
        scrappingService.setAdvertismentExtractorResults(advertismentExtractorTasks);
        scrappingService.scrapSite();

        tasksListener.setAdvertismentExtractorResults(advertismentExtractorTasks);
        tasksListener.setAdUrlsInProgress(scrappingService.getAdvertismentUrlsInProgress());

        Timer timer = new Timer();
        ExecutorService executor = executorService.getExecutor();
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
        printExecutionTime(launchResult.getStartTime());
    }

    private void printEnvironmentInfo()
    {
        LOGGER.info("");
        LOGGER.info("=== === Printing execution environment info  === ===");
        System.getenv().forEach((k, v) -> logEntry(k, v));
        System.getProperties().forEach((k, v) -> logEntry(k, v));
        LOGGER.info("=== === === === === === === === === === ===  === ===");
        LOGGER.info("");
    }

    private void printProgramInfo() throws IOException
    {
        LOGGER.info("=== === Printing program info   === ===");
        LOGGER.info("");
        LOGGER.info("App version: {}", APP_VERSION);
        LOGGER.info("");
        LOGGER.info("... ... Printing App properties ... ... ");
        Properties props = PropertiesLoaderUtils.loadProperties(new FileSystemResource("settings/realty.properties"));
        props.forEach((k, v) -> logEntry(k, v));
        LOGGER.info("... ... Printing Source properties ... ... ");
        props = PropertiesLoaderUtils.loadProperties(new FileSystemResource(sourceProperties));
        props.forEach((k, v) -> logEntry(k, v));
        LOGGER.info("... ... ... ... ... ... ... ... ... ... ");
    }

    private void printExecutionTime(Instant startTime)
    {
        Instant now = Instant.now();
        launchResult.setEndTime(now);
        Duration d = Duration.between(startTime, now);
        String message = String.format("Total time taken: %s hrs %s mins %s secs", d.toHours(), d.toMinutes() % 60,
                d.getSeconds() % 60);
        LOGGER.info(message);
        launchResult.setMessage(message);
    }

    private static void logEntry(Object k, Object v)
    {
        LOGGER.info("{} = {}", k, v);
    }
}
