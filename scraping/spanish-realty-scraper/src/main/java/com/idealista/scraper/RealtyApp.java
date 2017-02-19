package com.idealista.scraper;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.search.SearchAttribute;
import com.idealista.scraper.service.IdealistaScrappingService;
import com.idealista.scraper.util.LoggingUtils;
import com.idealista.scraper.util.PropertiesLoader;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.xls.TasksListener;
import com.idealista.scraper.xls.XlsExporter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.Properties;
import java.util.Timer;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;

public class RealtyApp
{
    static
    {
        LoggingUtils.turnOffHtmlUnitWarnings();
    }

    public static void main(String[] args) throws InterruptedException, IOException
    {
        String baseUrl = "https://www.idealista.com/en/";
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(baseUrl);

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

        RealtyApp app = new RealtyApp();
        while (!executor.isTerminated() && !advertismentExtractorTasks.isEmpty())
        {
            synchronized (app)
            {
                app.wait(60000);
            }
        }
        timer.cancel();
        timer = null;
        webDriverProvider.destroy();
    }
}
