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
import com.idealista.scraper.util.RegexUtils;
import com.idealista.scraper.util.WebDriverUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FotocasaPaginator implements IPaginator
{
    private static final Logger LOGGER = LogManager.getLogger(FotocasaPaginator.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    private SearchActions searchActions = new SearchActions();
    private ClickActions clickActions = new ClickActions();

    @Override
    public Set<Category> getAllPageUrls(Category baseCategory)
    {
        URL url = baseCategory.getUrl();
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(url);
        WebDriverUtils.waitForJSToLoad(driver);

        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);

        List<WebElement> paginatorLinks = searchActions.findElementsByXpath("//div[@class='sui-Pagination']");
        if (!paginatorLinks.isEmpty())
        {
            LOGGER.info("Paginator1 is found: some amount of pages");
            List<WebElement> links = searchActions.findElementsByXpath(paginatorLinks,
                    "//ul[@class='sui-Pagination-list']//li");
            return getPaginatorLinks(links, baseCategory);
        }
        paginatorLinks = searchActions.findElementsByXpath("//div[@class='pagination']");
        if (!paginatorLinks.isEmpty())
        {
            LOGGER.info("Paginator2 is found: some amount of pages");
            List<WebElement> links = searchActions.findElementsByXpath(paginatorLinks, "//ul//li");
            return getPaginatorLinks(links, baseCategory);
        }
        List<WebElement> paginatorNoLinks = searchActions
                .findElementsByXpath("//div[@class='re-SearchresultNoResults']");
        if (!paginatorNoLinks.isEmpty())
        {
            LOGGER.info("Paginator is found: no pages. Returning empty collection...");
            return Collections.emptySet();
        }
        LOGGER.error("Shouldn't get to this line, returning empty collection");
        return Collections.emptySet();
    }

    private Set<Category> getPaginatorLinks(List<WebElement> links, Category baseCategory)
    {
        int size = links.size();
        System.out.println("lastPageIndex" + links.get(size - 2).getText());
        if (size == 1)
        {
            LOGGER.info("Only one page for the category found...");
            return Collections.singleton(baseCategory);
        }
        else
        {
            LOGGER.info("Several pages found");
            WebElement lastPageLink = links.get(size - 2);
            String lastPageIndex = lastPageLink.getText();
            int maxPageIndex = RegexUtils.extractBigNumber(lastPageIndex);
            String basePageUrl = searchActions.findElementsByXpath(lastPageLink, "/a").get(0).getAttribute("href");
            return generatePages(baseCategory, basePageUrl, maxPageIndex);
        }
    }

    private Set<Category> generatePages(Category baseCategory, String basePageUrl, int maxPageIndex)
    {
        Set<Category> searchPagesToProcess = new HashSet<>();
        IntStream.range(1, maxPageIndex + 1)
                .forEach(e -> searchPagesToProcess.add((generateCategory(baseCategory, basePageUrl, e))));
        LOGGER.info("Pages to process count: {}", searchPagesToProcess.size());
        return searchPagesToProcess;
    }

    private Category generateCategory(Category baseCategory, String basePagePath, int pageNumber)
    {
        String latitude = "?latitude=";
        String inputString = null;
        if (basePagePath.contains(latitude))
        {
            String[] parts = basePagePath.split("\\?latitude=");
            String pathWithPageNumber = parts[0];
            int lastIndexOf = pathWithPageNumber.lastIndexOf('/');
            pathWithPageNumber = pathWithPageNumber.substring(0, lastIndexOf + 1);
            String pathEnd = parts[1];
            inputString = pathWithPageNumber + pageNumber + latitude + pathEnd;
        }
        else if (basePagePath.contains("listado?crp="))
        {
            int lastIndexOf = basePagePath.lastIndexOf('=');
            inputString = cutPagePath(basePagePath, lastIndexOf, pageNumber);
        }
        else if (basePagePath.contains("/l/"))
        {
            int lastIndexOf = basePagePath.lastIndexOf('/');
            inputString = cutPagePath(basePagePath, lastIndexOf, pageNumber);
        }
        else
        {
            LOGGER.error("Could not determine basePage pattern to generate pages");
            return null;
        }
        try
        {
            return new Category(new URL(inputString), baseCategory);
        }
        catch (MalformedURLException e)
        {
            LOGGER.error("Error while creating CategoryURL from string: {}, error: {}", basePagePath, e);
        }
        return null;
    }

    private String cutPagePath(String basePagePath, int lastIndexOf, int pageNumber)
    {
        return basePagePath.substring(0, lastIndexOf + 1) + pageNumber;
    }
}
