package com.idealista.scraper.scraping;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.util.URLUtils;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.NavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

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

    private Category category;

    private WebDriverProvider webDriverProvider;
    private INavigateActions navigateActions;
    private ProxyMonitor proxyMonitor;

    public SearchPageProcessor(Category category, WebDriverProvider webDriverProvider, ProxyMonitor proxyMonitor,
            INavigateActions navigateActions)
    {
        this.category = category;
        this.webDriverProvider = webDriverProvider;
        this.proxyMonitor = proxyMonitor;
        this.navigateActions = navigateActions;
    }

    @Override
    public Set<Category> call()
    {
        URL page = category.getUrl();
        LOGGER.info("Processing search page: {}", page);
        WebDriver driver = webDriverProvider.get();
        driver = navigateActions.get(page);
        driver = proxyMonitor.checkForVerificationAndRestartDriver(driver);
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
        LOGGER.error("Search page is empty.. Smth bad happened, returning empty collection from page: {}", page);
        return Collections.emptySet();
    }
}
