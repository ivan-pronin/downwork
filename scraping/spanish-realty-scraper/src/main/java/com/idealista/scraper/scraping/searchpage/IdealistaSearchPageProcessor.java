package com.idealista.scraper.scraping.searchpage;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.util.URLUtils;
import com.idealista.scraper.webdriver.INavigateActions;
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

public class IdealistaSearchPageProcessor implements ISeachPageProcessor
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaSearchPageProcessor.class);

    private Category category;
    private WebDriverProvider webDriverProvider;
    private INavigateActions navigateActions;
    private ProxyMonitor proxyMonitor;

    public IdealistaSearchPageProcessor(Category category)
    {
        this.category = category;
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
            LOGGER.info("Page has been processed successfully: {}", page);
            return adUrls;
        }
        LOGGER.error("Search page is empty.. Smth bad happened, returning empty collection from page: {}", page);
        return Collections.emptySet();
    }

    public void setNavigateActions(INavigateActions navigateActions)
    {
        this.navigateActions = navigateActions;
    }

    public void setProxyMonitor(ProxyMonitor proxyMonitor)
    {
        this.proxyMonitor = proxyMonitor;
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }
}
