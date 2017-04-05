package com.crunchbase.scraper.ui;

import com.crunchbase.scraper.model.HtmlData;
import com.crunchbase.scraper.util.FileUtils;
import com.crunchbase.scraper.util.WaitUtils;
import com.crunchbase.scraper.util.WebDriverUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StartPage
{
    private static final Logger LOGGER = LogManager.getLogger(StartPage.class);
    private static final String HREF_ATTRIBUTE = "href";
    private static final String TAG_A = "a";

    private ClickActions clickActions = new ClickActions();
    private SearchActions searchActions = new SearchActions();
    private WebDriver driver;
    private PageLoader pageLoader;
    private String targetFile;

    public Set<HtmlData> getSingleAutocompleteResult(String title)
    {
        printStartSearching("Autocomplete - Single ", title);
        List<WebElement> fieldSearch = searchActions.findElementsById("input-0");
        if (fieldSearch.isEmpty())
        {
            LOGGER.error("Failed to find INPUT-0 field");
            return createHtmldataError();
        }
        try
        {
            clickActions.setElementTextFast(fieldSearch, title);
        }
        catch (StaleElementReferenceException e)
        {
            fieldSearch = searchActions.findElementsById("input-0");
            clickActions.setElementTextSlowly(fieldSearch, title);
        }
        searchActions
                .findElementsByXpath(
                        "//div[@class='cb-background-white layout-align-start-center layout-row flex-none']")
                .get(0).click();
        WebElement inputSearchField = fieldSearch.get(0);
        inputSearchField.click();
        WaitUtils.sleepSeconds(this, 3);
        WebDriverUtils.waitForJSToLoad(driver);

        List<WebElement> autocomplete = searchActions
                .waitForElements(By.xpath("//ul[@class='md-autocomplete-suggestions topSearchMenu']//li"), 5);
        if (autocomplete.isEmpty())
        {
            LOGGER.error("Failed to wait for autocomplete dropdownm returning empty results");
            return Collections.emptySet();
        }
        clickFirstAutocompleteLink(inputSearchField);
        if (pageLoader.tryToLoadEntityDetaledPage())
        {
            HtmlData data = new HtmlData();
            try
            {
                data.setUrl(new URL(driver.getCurrentUrl()));
                String doc = driver.getPageSource();
                FileUtils.saveHtmlToFile(data, 1, title, doc, targetFile);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return Collections.singleton(data);
        }
        LOGGER.error("Could not find Single Autocomplete results for: {}, returning error", title);
        return Collections.emptySet();
    }

    private void clickFirstAutocompleteLink(WebElement inputSearchField)
    {
        LOGGER.info("Clicking by coordinates..." + inputSearchField.getLocation());
        new Actions(driver).moveToElement(inputSearchField, 50, 20).perform();
        new Actions(driver).click().perform();
        LOGGER.info("Clicked by coordinates!");
    }

    public Set<HtmlData> getFirstFiveAutocompleteResults(String title)
    {
        printStartSearching("Autocomplete", title);
        if (!pageLoader.tryToLoadStartPage())
        {
            LOGGER.error("PageLoader couldn't load start page. Returning empty marked collection");
            return createHtmldataError();
        }

        List<WebElement> fieldSearch = searchActions.findElementsById("q");
        if (fieldSearch.isEmpty())
        {
            fieldSearch = searchActions.findElementsById("input-0");
        }
        try
        {
            clickActions.setElementTextFastAndWait(fieldSearch, title);
        }
        catch (StaleElementReferenceException e)
        {
            fieldSearch = searchActions.findElementsById("q");
            if (fieldSearch.isEmpty())
            {
                fieldSearch = searchActions.findElementsById("input-0");
            }
            clickActions.setElementTextFastAndWait(fieldSearch, title);
        }

        Actions builder = new Actions(driver);
        builder.sendKeys(" ").perform();

        WebDriverUtils.waitForJSToLoad(driver);
        WaitUtils.sleep(this, 2000);

        List<WebElement> autocomplete = searchActions
                .waitForElements(By.xpath("//ul[@class='md-autocomplete-suggestions topSearchMenu']//li"), 5);
        if (autocomplete.isEmpty())
        {
            autocomplete = searchActions.waitForElements(By.xpath("//ul[@id='ui-id-1']//li"), 5);
        }

        Set<HtmlData> results = new HashSet<>();
        int count = 0;
        if (autocomplete.isEmpty())
        {
            LOGGER.error("No results were found for: {}", title);
            return Collections.emptySet();
        }
        for (WebElement item : autocomplete)
        {
            HtmlData data = new HtmlData();
            try
            {
                List<WebElement> link = searchActions.findElementsByTagName(Arrays.asList(item), TAG_A);
                String attribute = link.get(0).getAttribute(HREF_ATTRIBUTE);
                data.setUrl(new URL(attribute));
                LOGGER.info("Found new company URL: {}", attribute);
            }
            catch (StaleElementReferenceException | MalformedURLException e)
            {
                LOGGER.error("Error while getting URL for company: {}", e);
                LOGGER.error("Trying to get href again ... ");
                List<WebElement> link = searchActions.findElementsByTagName(Arrays.asList(item), TAG_A);
                String attribute = link.get(0).getAttribute(HREF_ATTRIBUTE);
                try
                {
                    data.setUrl(new URL(attribute));
                }
                catch (MalformedURLException e1)
                {
                    LOGGER.error("Error while getting URL for company: {}", e1);
                }
            }
            results.add(data);
            count++;
            if (count > 4)
            {
                break;
            }
        }
        if (!fieldSearch.isEmpty())
        {
            clickActions.setElementTextSlowly(fieldSearch, "");
            builder.sendKeys(Keys.ESCAPE).perform();
            searchActions.waitForElementDisappear(By.xpath("//ul[@id='ui-id-1']//li"), 5);
        }
        return results;
    }

    private Set<HtmlData> createHtmldataError()
    {
        HtmlData data = new HtmlData();
        data.setFileName("ERROR");
        return new HashSet<>(Arrays.asList(data));
    }

    public Set<HtmlData> getFirstFiveSearchResults(String title)
    {
        printStartSearching("Main search", title);
        List<WebElement> fieldSearch = searchActions.findElementsById("q");
        if (fieldSearch.isEmpty())
        {
            fieldSearch = searchActions.findElementsById("input-0");
            clickActions.setElementTextFast(fieldSearch, title);
            List<WebElement> input = searchActions.findElementsByXpath("//top-search//form//input");
            input.get(0).sendKeys(Keys.ENTER);
        }
        else
        {
            clickActions.setElementTextFast(fieldSearch, title);
            fieldSearch.get(0).submit();
        }

        if (!pageLoader.tryToLoadSearchResultsPage())
        {
            LOGGER.error("PageLoader couldn't load search results page. Returning empty marked collection");
            return createHtmldataError();
        }

        List<WebElement> resultsInfo = searchActions.findElementsByXpath("//results-info/h3[contains(.,'result')]");
        if (noResultsFound(resultsInfo))
        {
            LOGGER.warn("No Search results were found for company: {}", title);
            return Collections.emptySet();
        }
        List<WebElement> links = new ArrayList<>();
        if (searchActions.waitForElement(By.xpath("//grid//div[@cb-grid-header]"), 10) != null)
        {
            links = searchActions.waitForElements(By.xpath("//span[@class='identifier layout-row flex']"
                    + "//a[contains(@ng-if,'fieldFormatter.identifiers[0].href.length > 0')]"), 10);
        }
        else
        {
            LOGGER.warn("No Search results were found for company: {}", title);
            return Collections.emptySet();
        }
        int size = links.size();

        if (size > 5)
        {
            LOGGER.warn("Too many search results found. Will use autocomplete");
            return getSingleAutocompleteResult(title);
        }

        Set<HtmlData> results = new HashSet<>();
        int count = 0;
        for (WebElement item : links)
        {
            HtmlData data = new HtmlData();
            try
            {
                String attribute = item.getAttribute(HREF_ATTRIBUTE);
                data.setUrl(new URL(attribute));
                LOGGER.info("Found new company URL: {}", attribute);
            }
            catch (StaleElementReferenceException | MalformedURLException e)
            {
                LOGGER.error("Error while getting URL for company: {}", e);
            }
            results.add(data);
            count++;
            if (count > 4)
            {
                break;
            }
        }
        return results;
    }

    private boolean noResultsFound(List<WebElement> resultsInfo)
    {
        return resultsInfo != null && !resultsInfo.isEmpty() && resultsInfo.get(0).getText().indexOf("0 results") == 0;
    }

    public void setWebDriver(WebDriver driver)
    {
        this.driver = driver;
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
    }

    public void waitToLoad()
    {
        searchActions.waitForElement(By.xpath("//h2[text()='Trending on Crunchbase']"), 30);
    }

    public void waitForSearchToLoad()
    {
        searchActions.waitForElement(By.id("main-header"), 10);
    }

    private void printStartSearching(String prefix, String title)
    {
        LOGGER.info("{}: Searching for company: {}", prefix, title);
    }

    public void waitToLoadFast()
    {
        searchActions.waitForElement(By.xpath("//h2[text()='Trending on Crunchbase']"), 30);
    }

    public void setPageLoader(PageLoader pageLoader)
    {
        this.pageLoader = pageLoader;
    }

    public void setTargetFile(String targetFile)
    {
        this.targetFile = targetFile;
    }
}
