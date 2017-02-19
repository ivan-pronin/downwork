package com.idealista.scraper.model;

import com.idealista.scraper.util.URLUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Callable;

public class CounteredSearchPageProcessor implements Callable<Set<URL>>
{
    private static final Logger LOGGER = LogManager.getLogger(CounteredSearchPageProcessor.class);
    private static int requestCounter;
    private WebDriverProvider webDriverProvider;
    private URL pageUrl;

    public CounteredSearchPageProcessor(WebDriverProvider webDriverProvider, URL pageUrl)
    {
        this.webDriverProvider = webDriverProvider;
        this.pageUrl = pageUrl;
    }

    @Override
    public Set<URL> call()
    {
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(pageUrl);
        String currentUrl = driver.getCurrentUrl();
        if (currentUrl.contains("checkvalidation"))
        {
            LOGGER.error("CHECK_VALIDATION page is opened at counter: {}", requestCounter);
            return null;
        }
        LOGGER.info("Counter at page {} : {}", driver.getCurrentUrl(), ++requestCounter);
        WebElement divContainer = driver.findElement(By.xpath("//div[@class='items-container']"));
        if (divContainer != null)
        {
            LOGGER.info("Item container found: {}", divContainer);
            List<WebElement> ads = divContainer.findElements(By.xpath(".//article[not(@class)]"));
            Set<URL> adUrls = new HashSet<>();
            for (WebElement ad : ads)
            {
                WebElement infoLink = ad
                        .findElement(By.xpath(".//div[@class='item-info-container']//a[contains(@class,'item-link')]"));
                if (infoLink != null)
                {
                    String attribute = infoLink.getAttribute("href");
                    adUrls.add(URLUtils.generateUrl(attribute));
                }
            }
            LOGGER.info("Ads count: {}", ads.size());
            return adUrls;
        }
        return Collections.emptySet();
    }

    public static int getRequestCounter()
    {
        return requestCounter;
    }
}
