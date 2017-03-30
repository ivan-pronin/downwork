package com.crunchbase.scraper.ui;

import com.crunchbase.scraper.model.HtmlData;
import com.crunchbase.scraper.service.CrunchbaseScraperService;
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

    public Set<HtmlData> getFirstFiveAutocompleteResults(String title)
    {
        printStartSearching(title);
        driver.get(CrunchbaseScraperService.CRUNCHBASE_COM);
        WebDriverUtils.waitForJSToLoad(driver);

        if (searchActions.waitForElement(By.xpath("//h2[text()='Trending on Crunchbase']"), 10) == null)
        {
            LOGGER.warn("Failed to wait for //h2[text()='Trending on Crunchbase'] >> RETURNING empty set");
            return Collections.emptySet();
        }

        List<WebElement> fieldSearch = searchActions.findElementsById("q");
        if (fieldSearch.isEmpty())
        {
            fieldSearch = searchActions.findElementsById("input-0");
        }
        try
        {
            clickActions.setElementTextSlowly(fieldSearch, title);
        }
        catch (StaleElementReferenceException e)
        {
            fieldSearch = searchActions.findElementsById("q");
            clickActions.setElementTextSlowly(fieldSearch, title);
        }

        Actions builder = new Actions(driver);
        builder.sendKeys(" ").perform();

        WebDriverUtils.waitForJSToLoad(driver);
        WaitUtils.sleep(this, 2000);

        List<WebElement> autocomplete = searchActions
                .waitForElements(By.xpath("//ul[@class='md-autocomplete-suggestions topSearchMenu']//li"), 5);
        if (autocomplete == null)
        {
            autocomplete = searchActions.waitForElements(By.xpath("//ul[@id='ui-id-1']//li"), 5);
        }

        Set<HtmlData> results = new HashSet<>();
        int count = 0;
        if (autocomplete == null)
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
            WaitUtils.sleep(this, 3000);
        }
        return results;
    }

    public Set<HtmlData> getFirstFiveSearchResults(String title)
    {
        printStartSearching(title);
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
        searchActions.waitForElementDisappear(By.xpath("//h2[text()='Companies']"), 5);
        WebDriverUtils.waitForPageLoad(driver);
        WebDriverUtils.waitForJSToLoad(driver);
        searchActions.waitForElement(By.xpath("//grid//div[@cb-grid-header]"), 10);

        searchActions.waitForElements(
                By.xpath(
                        "//span[@class='identifier layout-row flex']//a[contains(@ng-if,'fieldFormatter.identifiers[0].href.length > 0')]"),
                10);
        List<WebElement> links = searchActions.findElementsByXpath(
                "//span[@class='identifier layout-row flex']//a[contains(@ng-if,'fieldFormatter.identifiers[0].href.length > 0')]");
        int size = links.size();

        if (size > 5)
        {
            LOGGER.warn("Too many search results found. Will use autocomplete");
            return getFirstFiveAutocompleteResults(title);
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

    public void setWebDriver(WebDriver driver)
    {
        this.driver = driver;
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
    }

    public void waitToLoad()
    {
        searchActions.waitForElement(By.xpath("//h2[text()='Trending on Crunchbase']"), 60);
    }

    private void printStartSearching(String title)
    {
        LOGGER.info("    ");
        LOGGER.info("Searching for company: {}", title);
    }
}
