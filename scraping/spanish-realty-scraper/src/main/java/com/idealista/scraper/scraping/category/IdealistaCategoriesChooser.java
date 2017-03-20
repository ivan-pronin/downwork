package com.idealista.scraper.scraping.category;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.IdealistaStartPage;
import com.idealista.scraper.ui.page.MapPage;
import com.idealista.scraper.ui.page.SearchPage;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
public class IdealistaCategoriesChooser extends AbstractCategoriesChooser implements ICategoriesChooser
{
    private class CategoryBySearchAndFilterAttributes implements Callable<Category>
    {
        private String operation;
        private String typology;
        private String location;
        private String province;
        private FilterAttributes filterAttributes;

        private CategoryBySearchAndFilterAttributes(String operation, String typology, String location,
                FilterAttributes filterAttributes, String province)
        {
            this.operation = operation;
            this.typology = typology;
            this.location = location;
            this.province = province;
            this.filterAttributes = filterAttributes;
        }

        @Override
        public Category call() throws Exception
        {
            if ("International".equalsIgnoreCase(location))
            {
                LOGGER.info("Skipping International site by now...");
                return null;
            }
            WebDriver driver = webDriverProvider.get();
            driver.navigate().to(getMainPageLocalizedUrl());
            IdealistaStartPage startPage = new IdealistaStartPage();
            startPage.setWebDriver(driver);
            startPage.selectOperation(operation);
            if (!startPage.getAvailableTypologies().contains(typology))
            {
                LOGGER.error("Specified Operation + Type combo is not available: {} + {}", operation, typology);
                return null;
            }
            selectOptionsAndStartSearch(startPage, operation, typology, location);
            if (proxyMonitor.ifVerificationAppered(driver))
            {
                driver = proxyMonitor.restartDriver();
                driver.navigate().to(getMainPageLocalizedUrl());
                selectOptionsAndStartSearch(startPage, operation, typology, location);
            }
            MapPage mapPage = new MapPage();
            mapPage.setWebDriver(driver);
            mapPage.clickShowAll();

            SearchPage searchPage = new SearchPage();
            searchPage.setWebDriver(driver);
            searchPage.selectProvince(province);
            searchPage.applyPublicationDateFilter(filterAttributes);

            String categoryUrl = driver.getCurrentUrl();
            LOGGER.info("Found new category url: {}", categoryUrl);
            mapPage.clickIdealistaLink();
            if (proxyMonitor.ifVerificationAppered(driver))
            {
                driver = proxyMonitor.restartDriver();
                driver.navigate().to(getMainPageLocalizedUrl());
            }
            Category category = new Category(new URL(categoryUrl), location, operation, typology);
            category.setProvince(province);
            return category;
        }
    }
    private static final Logger LOGGER = LogManager.getLogger(IdealistaCategoriesChooser.class);

    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.IDEALISTA;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private ProxyMonitor proxyMonitor;

    @Autowired
    private AppConfig appConfig;

    @Override
    public Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext)
    {
        SearchAttributes searchAttributes = searchFilterContext.getSearchAttributes();
        Set<String> userOperations = searchAttributes.getOperations();
        Set<String> userTypologies = searchAttributes.getTypologies();
        Set<String> userLocations = searchAttributes.getLocations();
        IdealistaStartPage startPage = new IdealistaStartPage();
        startPage.setWebDriver(webDriverProvider.get());

        Queue<Callable<Category>> results = new ConcurrentLinkedQueue<>();

        for (String operation : userOperations)
        {
            startPage.selectOperation(operation);
            Set<String> availableTypologies = startPage.getAvailableTypologies();

            for (String typology : availableTypologies)
            {
                if (userTypologies.contains(typology))
                {
                    startPage.selectTypology(typology);
                    Set<String> availableLocations = startPage.getAvailableLocations();
                    for (String location : availableLocations)
                    {
                        if (userLocations.contains(location))
                        {
                            startPage.selectLocation(location);
                            results.add(new CategoryBySearchAndFilterAttributes(operation, typology, location,
                                    searchFilterContext.getFilterAttributes(), searchFilterContext.getProvince()));
                        }
                    }
                }
            }
        }
        return executeAndGetResults(results);
    }

    public void setProxyMonitor(ProxyMonitor proxyMonitor)
    {
        this.proxyMonitor = proxyMonitor;
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }

    private String getMainPageLocalizedUrl()
    {
        return SCRAP_TARGET.getMainPageLocalizedUrl(appConfig.getLanguage());
    }

    private void selectOptionsAndStartSearch(IdealistaStartPage startPage, String operationName, String typologyName,
            String location)
    {
        startPage.selectOperation(operationName);
        startPage.selectTypology(typologyName);
        startPage.selectLocation(location);
        startPage.clickSearch();
    }
}
