package com.idealista.scraper.scraping.paginator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.ui.ClickActions;
import com.idealista.scraper.ui.SearchActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class VibboPaginator implements IPaginator
{
    private static final Logger LOGGER = LogManager.getLogger(VibboPaginator.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Override
    public Set<Category> getAllPageUrls(Category baseCategory)
    {
        URL url = baseCategory.getUrl();
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(url);

        SearchActions searchActions = new SearchActions();
        ClickActions clickActions = new ClickActions();
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);

        List<WebElement> paginatorLinks = searchActions.findElementsById("result_container");
        List<WebElement> lastLink = searchActions.findElementsByXpath(paginatorLinks,
                "//span[@class='icon-ics_navigation_ic_Last']");
        if (!lastLink.isEmpty())
        {
            clickActions.click(lastLink);
        }
        else
        {
            // handle case when pages count is <=6 (no "last" page link)
            LOGGER.warn("Didn't find pagination for category page: {}. The category has only a single page", url);
            return Collections.singleton(baseCategory);
        }
        String lastPageUrl = driver.getCurrentUrl();
        int lastIndexOfEquals = lastPageUrl.lastIndexOf('=');
        String basePagePath = lastPageUrl.substring(0, lastIndexOfEquals + 1);
        String number = lastPageUrl.substring(lastIndexOfEquals + 1);
        int lastPageNumber = Integer.parseInt(number);
        Set<Category> searchPagesToProcess = new HashSet<>();
        IntStream.range(1, lastPageNumber + 1)
                .forEach(e -> searchPagesToProcess.add((generateCategory(baseCategory, basePagePath, e))));
        LOGGER.info("Pages to process count: {}", searchPagesToProcess.size());
        return searchPagesToProcess;
    }

    private Category generateCategory(Category baseCategory, String basePagePath, int pageNumber)
    {
        try
        {
            return new Category(new URL(basePagePath + pageNumber), baseCategory);
        }
        catch (MalformedURLException e)
        {
            LOGGER.error("Error while creating CategoryURL from string: {}, error: {}", basePagePath, e);
        }
        return null;
    }
}
