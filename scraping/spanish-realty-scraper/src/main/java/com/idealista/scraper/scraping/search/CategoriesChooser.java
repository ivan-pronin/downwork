package com.idealista.scraper.scraping.search;

import com.idealista.scraper.RealtyApp;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.service.model.FilterAttributes;
import com.idealista.scraper.ui.page.MapPage;
import com.idealista.scraper.ui.page.SearchPage;
import com.idealista.scraper.ui.page.StartPage;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

@Component
public class CategoriesChooser
{
    private static final Logger LOGGER = LogManager.getLogger(CategoriesChooser.class);

    @Autowired
    private WebDriverProvider webDriverProvider;
    
    @Autowired
    private StartPage startPage;
    
    @Autowired
    private MapPage mapPage;
    
    @Autowired
    private SearchPage searchPage;
    
    @Autowired
    private ProxyMonitor proxyMonitor;
    
    @Autowired
    private RealtyApp realtyApp;

    public Set<String> getAllCategoriesUrls()
    {
        Set<String> categoriesUrls = new HashSet<>();
        WebDriver driver = webDriverProvider.get();
        startPage.setWebDriver(driver);
        Set<String> availableOperations = startPage.getAvailableOperations();
        for (String operation : availableOperations)
        {
            startPage.selectOperation(operation);
            Set<String> availableTylopogies = startPage.getAvailableTypologies();
            for (String typology : availableTylopogies)
            {
                startPage.selectOperation(operation);
                startPage.selectTypology(typology);
                Set<String> availableLocations = startPage.getAvailableLocations();
                for (String location : availableLocations)
                {
                    selectOptionsAndStartSearch(startPage, operation, typology, location);
                    mapPage.setWebDriver(driver);
                    mapPage.clickShowAll();
                    String currentUrl = driver.getCurrentUrl();
                    categoriesUrls.add(currentUrl);
                    LOGGER.info("Added new category url: {}", currentUrl);
                    mapPage.clickIdealistaLink();
                    if (proxyMonitor.ifVerificationAppered(driver))
                    {
                        driver = proxyMonitor.restartDriver();
                        driver.navigate().to(realtyApp.getMainLocalizedPageUrl());
                    }
                }
            }
        }
        return categoriesUrls;
    }

    public Set<String> getCategoriesUrlsByOperation(String operationName)
    {
        Set<String> categoriesUrls = new HashSet<>();
        WebDriver driver = webDriverProvider.get();
        startPage.setWebDriver(driver);
        startPage.selectOperation(operationName);
        for (String typology : startPage.getAvailableTypologies())
        {
            startPage.selectOperation(operationName);
            startPage.selectTypology(typology);
            Set<String> availableLocations = startPage.getAvailableLocations();
            for (String location : availableLocations)
            {
                selectOptionsAndStartSearch(startPage, operationName, typology, location);
                mapPage.setWebDriver(driver);
                mapPage.clickShowAll();
                String currentUrl = driver.getCurrentUrl();
                categoriesUrls.add(currentUrl);
                LOGGER.info("Added new category url: {}", currentUrl);
                mapPage.clickIdealistaLink();
                if (proxyMonitor.ifVerificationAppered(driver))
                {
                    driver = proxyMonitor.restartDriver();
                    driver.navigate().to(realtyApp.getMainLocalizedPageUrl());
                }
            }
        }
        return categoriesUrls;
    }

    public class CategoryBySearchAndFilterAttributes implements Callable<Category>
    {
        private String operation;
        private String typology;
        private String location;
        private String province;
        private FilterAttributes filterAttributes;

        public CategoryBySearchAndFilterAttributes(String operation, String typology, String location,
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
            driver.navigate().to(realtyApp.getMainLocalizedPageUrl());
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
                driver.navigate().to(realtyApp.getMainLocalizedPageUrl());
                selectOptionsAndStartSearch(startPage, operation, typology, location);
            }
            mapPage.setWebDriver(driver);
            mapPage.clickShowAll();
            
            searchPage.setWebDriver(driver);
            searchPage.selectProvince(province);
            searchPage.applyPublicationDateFilter(filterAttributes);
            
            String categoryUrl = driver.getCurrentUrl();
            LOGGER.info("Found new category url: {}", categoryUrl);
            mapPage.clickIdealistaLink();
            if (proxyMonitor.ifVerificationAppered(driver))
            {
                driver = proxyMonitor.restartDriver();
                driver.navigate().to(realtyApp.getMainLocalizedPageUrl());
            }
            Category category = new Category(new URL(categoryUrl), location, operation, typology);
            category.setProvince(province);
            return category;
        }
    }
    
    public Set<String> getCategoriesUrlsByOperationAndTypology(String operationName, String typologyName)
    {
        Set<String> categoriesUrls = new HashSet<>();
        WebDriver driver = webDriverProvider.get();
        startPage.setWebDriver(driver);
        startPage.selectOperation(operationName);
        if (!startPage.getAvailableTypologies().contains(typologyName))
        {
            LOGGER.error("Specified Operation + Type combo is not available: {} + {}", operationName, typologyName);
            return Collections.emptySet();
        }
        for (String location : startPage.getAvailableLocations())
        {
            selectOptionsAndStartSearch(startPage, operationName, typologyName, location);
            if (proxyMonitor.ifVerificationAppered(driver))
            {
                driver = proxyMonitor.restartDriver();
                driver.navigate().to(realtyApp.getMainLocalizedPageUrl());
                continue;
            }
            mapPage.setWebDriver(driver);
            mapPage.clickShowAll();
            String currentUrl = driver.getCurrentUrl();
            categoriesUrls.add(currentUrl);
            LOGGER.info("Added new category url: {}", currentUrl);
            mapPage.clickIdealistaLink();
            if (proxyMonitor.ifVerificationAppered(driver))
            {
                driver = proxyMonitor.restartDriver();
                driver.navigate().to(realtyApp.getMainLocalizedPageUrl());
            }
        }
        return categoriesUrls;
    }

    public Set<String> getCategoryUrlByOperationTypologyAndLocation(String operationName, String typologyName,
            String locationName)
    {
        Set<String> categoriesUrls = new HashSet<>();
        WebDriver driver = webDriverProvider.get();
        startPage.setWebDriver(driver);
        startPage.selectOperation(operationName);
        if (!startPage.getAvailableTypologies().contains(typologyName))
        {
            LOGGER.error("Specified Operation + Type combo is not available: {} + {}", operationName, typologyName);
            return Collections.emptySet();
        }
        selectOptionsAndStartSearch(startPage, operationName, typologyName, locationName);
        if (proxyMonitor.ifVerificationAppered(driver))
        {
            driver = proxyMonitor.restartDriver();
            driver.navigate().to(realtyApp.getMainLocalizedPageUrl());
            selectOptionsAndStartSearch(startPage, operationName, typologyName, locationName);
        }
        mapPage.setWebDriver(driver);
        mapPage.clickShowAll();
        String currentUrl = driver.getCurrentUrl();
        LOGGER.info("Added new category url: {}", currentUrl);
        mapPage.clickIdealistaLink();
        if (proxyMonitor.ifVerificationAppered(driver))
        {
            driver = proxyMonitor.restartDriver();
            driver.navigate().to(realtyApp.getMainLocalizedPageUrl());
        }
        return categoriesUrls;
    }

    private void selectOptionsAndStartSearch(StartPage startPage, String operationName, String typologyName,
            String location)
    {
        startPage.selectOperation(operationName);
        startPage.selectTypology(typologyName);
        startPage.selectLocation(location);
        startPage.clickSearch();
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }

    public void setProxyMonitor(ProxyMonitor proxyMonitor)
    {
        this.proxyMonitor = proxyMonitor;
    }
}
