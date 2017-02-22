package com.idealista.scraper.page;

import com.idealista.scraper.proxy.ProxyMonitor;
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

public class SearchPageProcessor implements Callable<Set<URL>>
{
    private static final Logger LOGGER = LogManager.getLogger(SearchPageProcessor.class);
    private static int requestsCounter;
    private WebDriverProvider webDriverProvider;
    private URL pageUrl;
    private ProxyMonitor proxyMonitor = new ProxyMonitor();

    public SearchPageProcessor(WebDriverProvider webDriverProvider, URL pageUrl)
    {
        this.webDriverProvider = webDriverProvider;
        this.pageUrl = pageUrl;
    }

    @Override
    public Set<URL> call()
    {
        LOGGER.info("SearchPageProcessor is working at page: {}", pageUrl);
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(pageUrl);
        driver = proxyMonitor.checkForVerificationAndRestartDriver(driver, webDriverProvider);
        List<WebElement> divContainer = driver.findElements(By.xpath("//div[@class='items-container']"));
        if (!divContainer.isEmpty())
        {
            WebElement container = divContainer.get(0);
            LOGGER.info("Item container found: {}", divContainer);
            List<WebElement> ads = container.findElements(By.xpath(".//article[not(@class)]"));
            Set<URL> adUrls = new HashSet<>();
            for (WebElement ad : ads)
            {
                List<WebElement> infoLink = ad.findElements(
                        By.xpath(".//div[@class='item-info-container']//a[contains(@class,'item-link')]"));
                if (!infoLink.isEmpty())
                {
                    String attribute = infoLink.get(0).getAttribute("href");
                    adUrls.add(URLUtils.generateUrl(attribute));
                }
            }
            LOGGER.info("Ads count: {}", ads.size());
            return adUrls;
        }
        return Collections.emptySet();
    }

    public static int getRequestsCounter()
    {
        return requestsCounter;
    }
}
