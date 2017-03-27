package com.crunchbase.scraper.service;

import com.crunchbase.scraper.model.Company;
import com.crunchbase.scraper.model.HtmlData;
import com.crunchbase.scraper.ui.SearchActions;
import com.crunchbase.scraper.ui.StartPage;
import com.crunchbase.scraper.util.CsvUtils;
import com.crunchbase.scraper.util.WaitUtils;
import com.crunchbase.scraper.webdriver.WebDriverProvider;

import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Component
public class CrunchbaseScraperService
{
    public static final String CRUNCHBASE_COM = "https://www.crunchbase.com/#/home/index";

    private static final Logger LOGGER = LogManager.getLogger(CrunchbaseScraperService.class);

    @Value("${srcFile}")
    private String srcFile;

    @Value("${targetFile}")
    private String targetFile;

    @Value("${readStartRow}")
    private int readStartRow;

    @Value("${readEndRow}")
    private int readEndRow;

    @Autowired
    private WebDriverProvider webDriverProvider;

    public void scrap()
    {
        Set<Company> companies = new LinkedHashSet<>();
        Set<String> companyTitles = CsvUtils.readCsvToString(srcFile, readStartRow, readEndRow);
        companyTitles.forEach(e -> companies.add(new Company(e)));

        LOGGER.info("Companies size: {}", companies.size());
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(CRUNCHBASE_COM);

        StartPage startPage = new StartPage();
        startPage.setWebDriver(driver);

        startPage.waitToLoad();
        for (Company company : companies)
        {
            Set<HtmlData> companyData = startPage.getFirstFiveSearchResults(company.getTitle());
            company.setHtmlData(companyData);
        }

        for (Company company : companies)
        {
            Set<HtmlData> htmlData = company.getHtmlData();
            if (htmlData.isEmpty())
            {
                LOGGER.warn("Skipping HTML getting for company since no URLs were found: {}", company.getTitle());
                HtmlData item = new HtmlData();
                item.setFileName("NO_SEARCH_RESULTS_FOUND");
                htmlData = new HashSet<>();
                htmlData.add(item);
                company.setHtmlData(htmlData);
                continue;
            }
            int counter = 1;
            for (HtmlData data : htmlData)
            {
                processEachDataItem(company, data, counter++);
            }
        }
        CsvUtils.updateInputFile(companies, srcFile, targetFile);
    }

    private void processEachDataItem(Company company, HtmlData data, int counter)
    {
        String doc = loadDocument(data.getUrl());
        String nameAppender = counter == 1 ? "" : "_" + counter;
        String fileName = company.getTitle() + nameAppender + ".html";
        try
        {
            String subFolder = targetFile.split(".")[0];
            FileUtils.writeStringToFile(new File("./htmlFiles/" + subFolder + "/" + fileName), doc,
                    Charset.defaultCharset());
            data.setFileName(fileName);
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
        WaitUtils.sleep(this, 2000);
    }

    private String loadDocument(URL url)
    {
        WebDriver webDriver = webDriverProvider.get();
        webDriver.get(url.toString());
        SearchActions searchActions = new SearchActions();
        searchActions.setWebDriver(webDriver);
        searchActions.waitForElement(By.id("anchor-title"), 10);
        LOGGER.info("Hope the page was loaded, grabbing");
        return webDriver.getPageSource();
    }
}
