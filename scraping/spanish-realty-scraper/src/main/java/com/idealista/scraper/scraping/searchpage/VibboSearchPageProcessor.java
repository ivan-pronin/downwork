package com.idealista.scraper.scraping.searchpage;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.ui.SearchActions;
import com.idealista.scraper.util.URLUtils;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class VibboSearchPageProcessor implements ISeachPageProcessor
{
    private static final Logger LOGGER = LogManager.getLogger(VibboSearchPageProcessor.class);

    private Category category;
    private WebDriverProvider webDriverProvider;
    private INavigateActions navigateActions;
    private ProxyMonitor proxyMonitor;
    private SearchActions searchActions = new SearchActions();

    public VibboSearchPageProcessor(Category category)
    {
        this.category = category;
    }

    @Override
    public Set<Category> call() throws Exception
    {
        URL page = category.getUrl();
        LOGGER.info("Processing search page: {}", page);
        WebDriver driver = webDriverProvider.get();
        driver = navigateActions.get(page);
        driver = proxyMonitor.checkForVerificationAndRestartDriver(driver);
        searchActions.setWebDriver(driver);
        List<WebElement> divContainer = searchActions.findElementsById("hl");
        if (!divContainer.isEmpty())
        {
            List<WebElement> ads = searchActions.findElementsByXpath(divContainer,
                    "//div[@class='basicList flip-container list_ads_row  ']");
            Set<Category> adUrls = new HashSet<>();
            for (WebElement ad : ads)
            {
                List<WebElement> infoLink = searchActions.findElementsByXpath(ad,
                        "//div[@class='thumbnail_container']//a");
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
