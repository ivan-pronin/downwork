package com.idealista.scraper.page;

import com.idealista.scraper.proxy.ProxyMonitor;
import com.idealista.scraper.search.Category;
import com.idealista.scraper.util.URLUtils;
import com.idealista.scraper.webdriver.NavigateActions;
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

public class SearchPageProcessor implements Callable<Set<Category>>
{
    private static final Logger LOGGER = LogManager.getLogger(SearchPageProcessor.class);
    private static volatile int totalAdsCounter;
    private WebDriverProvider webDriverProvider;
    private Category category;
    private ProxyMonitor proxyMonitor = new ProxyMonitor();

    public SearchPageProcessor(WebDriverProvider webDriverProvider, Category category)
    {
        this.webDriverProvider = webDriverProvider;
        this.category = category;
    }

    @Override
    public Set<Category> call()
    {
        URL page = category.getUrl();
        LOGGER.info("Processing search category: {}", page);
        WebDriver driver = webDriverProvider.get();
        NavigateActions navigateActions = new NavigateActions(webDriverProvider);
        driver = navigateActions.get(page);
        driver = proxyMonitor.checkForVerificationAndRestartDriver(driver, webDriverProvider);
        List<WebElement> divContainer = driver.findElements(By.xpath("//div[@class='items-container']"));
        if (!divContainer.isEmpty())
        {
            WebElement container = divContainer.get(0);
            List<WebElement> ads = container.findElements(By.xpath(".//article[not(@class)]"));
            Set<Category> adUrls = new HashSet<>();
            for (WebElement ad : ads)
            {
                List<WebElement> infoLink = ad.findElements(
                        By.xpath(".//div[@class='item-info-container']//a[contains(@class,'item-link')]"));
                if (!infoLink.isEmpty())
                {
                    String attribute = infoLink.get(0).getAttribute("href");
                    adUrls.add(new Category(URLUtils.generateUrl(attribute), category));
                }
            }
            int adsCount = ads.size();
            totalAdsCounter += adsCount;
            LOGGER.info("Added new advertisment urls: {}, total ads count: {}", adsCount, totalAdsCounter);
            return adUrls;
        }
        return Collections.emptySet();
    }
}
