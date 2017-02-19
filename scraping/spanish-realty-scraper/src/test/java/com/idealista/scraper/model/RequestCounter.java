package com.idealista.scraper.model;

import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.concurrent.Callable;

public class RequestCounter implements Callable<Integer>
{
    private static final Logger LOGGER = LogManager.getLogger(RequestCounter.class);

    private static int requestCounter;

    private WebDriverProvider webDriverProvider;
    private URL pageUrl;

    public RequestCounter(WebDriverProvider webDriverProvider, URL pageUrl)
    {
        this.webDriverProvider = webDriverProvider;
        this.pageUrl = pageUrl;
    }

    @Override
    public Integer call() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(pageUrl);
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("checkvalidation"))
        {
            LOGGER.error("CHECK_VALIDATION page is opened at counter: {}", requestCounter);
            return requestCounter;
        }
        //LOGGER.info("Counter at page {} is {}", pageUrl, ++requestCounter);
        return requestCounter;
    }
}
