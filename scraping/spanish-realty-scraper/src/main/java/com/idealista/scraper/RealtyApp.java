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

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.executor.listener.TasksListener;
import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.service.IdealistaScrappingService;
import com.idealista.scraper.util.DateTimeUtil;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.stereotype.Component;

@Component
public class RealtyApp
{
    private static final Logger LOGGER = LogManager.getLogger(RealtyApp.class);
    private static final String MAIN_PAGE_URL = "https://www.idealista.com/";
    private static final String APP_VERSION = "rc-1.0.3b";

    private Instant startTime;

    @Value("${enableWebdriverLogging}")
    private boolean enableWebdriverLogging;

    @Value("${language}")
    private String language;

    @Autowired
    private TasksListener tasksListener;

    @Autowired
    private ExecutorServiceProvider executorService;

    @Autowired
    private IdealistaScrappingService idealistaService;

    @Autowired
    private WebDriverProvider webDriverProvider;

    public void initSystemProperties()
    {
        if (enableWebdriverLogging)
        {
            System.setProperty("webdriver.chrome.logfile",
                    "./logs/chromedriver_" + DateTimeUtil.getTimeStamp() + ".log");
        }
    }

    public void printInfo() throws IOException
    {
        LOGGER.info("Program started");
        startTime = Instant.now();
        printProgramInfo();
        printEnvironmentInfo();
    }

    public void run() throws InterruptedException, IOException
    {
        BlockingQueue<Future<Advertisment>> advertismentExtractorTasks = new LinkedBlockingQueue<>();
        idealistaService.setAdvertismentExtractorResults(advertismentExtractorTasks);
        idealistaService.scrapSite();

        tasksListener.setAdvertismentExtractorResults(advertismentExtractorTasks);
        tasksListener.setAdUrlsInProgress(idealistaService.getAdvertismentUrlsInProgress());

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
        printExecutionTime(startTime);
    }

    public String getMainLocalizedPageUrl()
    {
        String localizationSuffix = "";
        if (language.equalsIgnoreCase("en"))
        {
            localizationSuffix = "en/";
        }
        return MAIN_PAGE_URL + localizationSuffix;
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

    private static void printProgramInfo() throws IOException
    {
        LOGGER.info("=== === Printing program info   === ===");
        LOGGER.info("");
        LOGGER.info("App version: {}", APP_VERSION);
        LOGGER.info("");
        LOGGER.info("... ... Printing App properties ... ... ");
        Properties props = PropertiesLoaderUtils.loadProperties(new FileSystemResource("realty.properties"));
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

    public String getLanguage()
    {
        return language;
    }
}
