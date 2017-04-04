package com.crunchbase.scraper.service;

import com.crunchbase.scraper.executor.ExecutorServiceProvider;
import com.crunchbase.scraper.model.Company;
import com.crunchbase.scraper.model.HtmlData;
import com.crunchbase.scraper.ui.SearchActions;
import com.crunchbase.scraper.webdriver.INavigateActions;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

public class HtmlDataLoader implements Callable<Company>
{
    private static final Logger LOGGER = LogManager.getLogger(HtmlDataLoader.class);

    private Company inputCompany;
    private INavigateActions navigateActions;
    private String targetFile;

    @Override
    public Company call() throws Exception
    {
        Set<HtmlData> htmlData = inputCompany.getHtmlData();
        if (htmlData.isEmpty())
        {
            LOGGER.warn("Skipping HTML getting for company since no URLs were found: {}", inputCompany.getTitle());
            HtmlData item = new HtmlData();
            item.setFileName("NO_SEARCH_RESULTS_FOUND");
            htmlData = new HashSet<>();
            htmlData.add(item);
            inputCompany.setHtmlData(htmlData);
            return inputCompany;
        }
        int counter = 1;
        for (HtmlData data : htmlData)
        {
            processEachDataItem(inputCompany, data, counter++);
        }
        return inputCompany;
    }

    private String loadDocument(URL url)
    {
        LOGGER.info("Loading HTML document with url: {}", url);
        WebDriver driver = navigateActions.get(url);
        SearchActions searchActions = new SearchActions();
        searchActions.setWebDriver(driver);
        searchActions.waitForElement(By.id("anchor-title"), 10);
        LOGGER.info("Hope the page: {} was loaded, grabbing", url);
        return driver.getPageSource();
    }

    private void processEachDataItem(Company company, HtmlData data, int counter)
    {
        String title = company.getTitle();
        LOGGER.info("Processing all documents for: {}", title);
        URL url = data.getUrl();
        if (url == null)
        {
            LOGGER.warn("NULL url for company: {}, skipping HTML loading", title);
            return;
        }
        String doc = loadDocument(url);
        String nameAppender = counter == 1 ? "" : "_" + counter;
        String fileName = title + nameAppender + ".html";
        try
        {
            String subFolder = targetFile.split("\\.")[0];
            FileUtils.writeStringToFile(new File("./htmlFiles/" + subFolder + "/" + fileName), doc,
                    Charset.defaultCharset());
            data.setFileName(fileName);
        }
        catch (IOException e1)
        {
            LOGGER.error("Failed to write to file: {}", e1.getMessage());
        }
    }

    public void setInputCompany(Company inputCompany)
    {
        this.inputCompany = inputCompany;
    }

    public void setNavigateActions(INavigateActions navigateActions)
    {
        this.navigateActions = navigateActions;
    }

    public void setTargetFile(String targetFile)
    {
        this.targetFile = targetFile;
    }
}
