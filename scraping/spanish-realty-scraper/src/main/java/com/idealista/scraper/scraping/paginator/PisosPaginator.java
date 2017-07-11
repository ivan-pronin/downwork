package com.idealista.scraper.scraping.paginator;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.util.RegexUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public final class PisosPaginator implements IPaginator
{
    private static final Logger LOGGER = LogManager.getLogger(PisosPaginator.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Override
    public Set<Category> getAllPageUrls(Category baseCategory)
    {
        URL url = baseCategory.getUrl();
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(url);
        List<WebElement> pagination = driver.findElements(By.xpath("//div[@class='pager']"));
        if (pagination.isEmpty())
        {
            LOGGER.warn("Didn't find pagination for category page: {}. The category has only a single page", url);
            // When there are no pages in the category
            return Collections.singleton(baseCategory);
        }
        List<WebElement> pages = pagination.get(0).findElements(By.tagName("a"));
        if (pages.isEmpty())
        {
            LOGGER.error("Failed to find pagination links");
            return Collections.emptySet();
        }
        int totalPages = RegexUtils.extractBigNumber(pages.get(pages.size() - 2).getText());
        String basePagePath = driver.getCurrentUrl();
        Set<Category> searchPagesToProcess = new HashSet<>();
        IntStream.range(1, totalPages + 1)
                .forEach(e -> searchPagesToProcess.add((generateCategory(baseCategory, basePagePath, e))));
        LOGGER.info("Pages to process count: {}", searchPagesToProcess.size());
        return searchPagesToProcess;
    }

    private Category generateCategory(Category baseCategory, String basePagePath, int pageNumber)
    {
        try
        {
            return new Category(new URL(basePagePath + "/" + pageNumber), baseCategory);
        }
        catch (MalformedURLException e)
        {
            LOGGER.error("Error while creating CategoryURL from string: {}, error: {}", basePagePath, e);
        }
        return null;
    }
}
