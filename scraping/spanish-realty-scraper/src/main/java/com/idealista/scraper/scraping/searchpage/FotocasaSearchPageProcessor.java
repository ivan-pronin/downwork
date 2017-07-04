package com.idealista.scraper.scraping.searchpage;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.ui.SearchActions;
import com.idealista.scraper.util.URLUtils;
import com.idealista.scraper.util.WaitUtils;
import com.idealista.scraper.util.WebDriverUtils;

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

public class FotocasaSearchPageProcessor extends AbstractSearchPageProcessor
{
    private static final String CARD_PRIMARY_LOCATOR = "//div[@class='re-Card-primary']";
    private static final String FOTOS_LOCATOR = "//span[@class='re-SearchGridType-literal' and text()='Fotos']";
    private static final String ITEM_CONTAINER_LOCATOR = "//div[@class='re-Searchresult-item']";

    private static final Logger LOGGER = LogManager.getLogger(FotocasaSearchPageProcessor.class);

    public FotocasaSearchPageProcessor(Category category)
    {
        super(category);
    }

    @Override
    public Set<Category> call() throws Exception
    {
        Category category = getCategory();
        URL page = category.getUrl();
        LOGGER.info("Processing search page: {}", page);
        WebDriver driver = getWebDriverProvider().get();
        driver = getNavigateActions().get(page);
        setWebDriver(driver);
        SearchActions searchActions = getSearchActions();
        List<WebElement> divContainer = searchActions.findElementsById("results");
        if (!divContainer.isEmpty())
        {
            return getAdUrlsFromObraNuevaPage(divContainer, page);
        }
        divContainer = searchActions.findElementsByXpath("//div[@id='App']");
        if (!divContainer.isEmpty())
        {
            return getAdUrlsFromNormalPage(divContainer, page);
        }
        LOGGER.error("Search page is empty.. Smth bad happened, returning empty collection from page: {}", page);
        return Collections.emptySet();
    }

    private Set<Category> getAdUrlsFromNormalPage(List<WebElement> divContainer, URL page)
    {
        SearchActions searchActions = getSearchActions();
        List<WebElement> switchView = searchActions.findElementsByXpath(FOTOS_LOCATOR);
        getClickActions().click(switchView);
        String itemsLocator = ITEM_CONTAINER_LOCATOR + CARD_PRIMARY_LOCATOR;
        searchActions.waitForElements(By.xpath(itemsLocator), 10);
        List<WebElement> lazyies = searchActions
                .findElementsByXpath(ITEM_CONTAINER_LOCATOR + "//div[@class='LazyLoad']");
        for (int i = 4; i < lazyies.size(); i += 4)
        {
            scrollToElementAndWait(lazyies, i);
        }
        WebDriverUtils.waitForAllContentToLoad(getWebDriverProvider().get());
        String locator = ITEM_CONTAINER_LOCATOR + CARD_PRIMARY_LOCATOR + "/div/div[position()=2]//a";
        List<WebElement> items = searchActions.findElementsByXpath(divContainer, locator);
        System.out.println("ITEMS found: " + items.size());

        if (items.isEmpty())
        {
            LOGGER.warn("Could not find any AD urls on the search page: {}", page);
            return Collections.emptySet();
        }
        Set<Category> adUrls = new HashSet<>();
        for (WebElement link : items)
        {
            String attribute = link.getAttribute("href");
            if (attribute != null)
            {
                adUrls.add(new Category(URLUtils.generateUrl(attribute), getCategory()));
            }
            else
            {
                LOGGER.info("FOUND NULL CATEGORY at element {} on page: {}", link, page);
            }
        }
        LOGGER.info("Page has been processed successfully: {}", page);
        LOGGER.debug("List all found categories");
        adUrls.forEach(LOGGER::debug);
        return adUrls;
    }

    private void scrollToElementAndWait(List<WebElement> lazyies, int i)
    {
        getClickActions().scrollToElement(lazyies.get(i));
        WaitUtils.sleep(this, 500);
    }

    private Set<Category> getAdUrlsFromObraNuevaPage(List<WebElement> divContainer, URL page)
    {
        SearchActions searchActions = getSearchActions();
        List<WebElement> ads = searchActions.findElementsByXpath(divContainer,
                "//ul[@id='ctl00_content1_gridphotoson_listAdsOn']//li");
        if (ads.isEmpty())
        {
            ads = searchActions.findElementsByXpath(divContainer, "//table[@id='search-listing']//tr");
        }
        Set<Category> adUrls = new HashSet<>();
        for (WebElement ad : ads)
        {
            String attribute = ad.getAttribute("data-url");
            if (attribute != null)
            {
                adUrls.add(new Category(URLUtils.generateUrl(attribute), getCategory()));
            }
            else
            {
                LOGGER.info("FOUND NULL CATEGORY at element {} on page: {}", ad, page);
            }
        }
        LOGGER.info("Page has been processed successfully: {}", page);
        LOGGER.debug("List all found categories");
        adUrls.forEach(LOGGER::debug);
        return adUrls;
    }
}
