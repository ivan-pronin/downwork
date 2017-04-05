package com.crunchbase.scraper.service;

import com.crunchbase.scraper.model.Company;
import com.crunchbase.scraper.model.HtmlData;
import com.crunchbase.scraper.ui.PageLoader;
import com.crunchbase.scraper.ui.StartPage;
import com.crunchbase.scraper.util.WaitUtils;
import com.crunchbase.scraper.util.WebDriverUtils;
import com.crunchbase.scraper.webdriver.INavigateActions;
import com.crunchbase.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.util.Set;
import java.util.concurrent.Callable;

public class CompanyUrlScraper implements Callable<Company>
{
    private static final Logger LOGGER = LogManager.getLogger(CompanyUrlScraper.class);
    private Company inputCompany;
    private WebDriverProvider webDriverProvider;
    private PageLoader pageLoader;
    private static ThreadLocal<Boolean> loadedMainPage = ThreadLocal.withInitial(() -> Boolean.FALSE);
    private static volatile int waitSeconds;
    private String targetFile;

    @Override
    public Company call() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        StartPage page = new StartPage();
        page.setWebDriver(driver);
        page.setPageLoader(pageLoader);
        page.setTargetFile(targetFile);
        if (!loadedMainPage.get())
        {
            // trying to launch browsers one by one, not simultaneously
            LOGGER.info("Entered LOCK block...");
            waitSeconds+=10;
            WaitUtils.sleepSeconds(this, waitSeconds);
            driver.navigate().to(CrunchbaseScraperService.CRUNCHBASE_COM);
            page.waitToLoad();
            WebDriverUtils.waitForJSToLoad(driver);
            LOGGER.warn("First refreshing ... ");
            driver.navigate().refresh();
            WebDriverUtils.waitForJSToLoad(driver);
            page.waitToLoad();
        }
        loadedMainPage.set(true);
        Set<HtmlData> data = page.getFirstFiveSearchResults(inputCompany.getTitle());
        inputCompany.setHtmlData(data);
        return inputCompany;
    }

    public void setInputCompany(Company inputCompany)
    {
        this.inputCompany = inputCompany;
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
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
