package com.idealista.scraper.scraping.search;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.ui.page.MapPage;
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
    private ProxyMonitor proxyMonitor;

    public Set<String> getAllCategoriesUrls()
    {
        Set<String> categoriesUrls = new HashSet<>();
        WebDriver driver = webDriverProvider.get();
        StartPage startPage = new StartPage(driver);
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
                    MapPage mapPage = new MapPage(driver);
                    mapPage.clickShowAll();
                    String currentUrl = driver.getCurrentUrl();
                    categoriesUrls.add(currentUrl);
                    LOGGER.info("Added new category url: {}", currentUrl);
                    mapPage.clickIdealistaLink();
                    if (proxyMonitor.ifVerificationAppered(driver))
                    {
                        driver = proxyMonitor.restartDriver(webDriverProvider);
                        driver.navigate().to("https://www.idealista.com/");
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
        StartPage startPage = new StartPage(driver);
        startPage.selectOperation(operationName);
        for (String typology : startPage.getAvailableTypologies())
        {
            startPage.selectOperation(operationName);
            startPage.selectTypology(typology);
            Set<String> availableLocations = startPage.getAvailableLocations();
            for (String location : availableLocations)
            {
                selectOptionsAndStartSearch(startPage, operationName, typology, location);
                MapPage mapPage = new MapPage(driver);
                mapPage.clickShowAll();
                String currentUrl = driver.getCurrentUrl();
                categoriesUrls.add(currentUrl);
                LOGGER.info("Added new category url: {}", currentUrl);
                mapPage.clickIdealistaLink();
                if (proxyMonitor.ifVerificationAppered(driver))
                {
                    driver = proxyMonitor.restartDriver(webDriverProvider);
                    driver.navigate().to("https://www.idealista.com/");
                }
            }
        }
        return categoriesUrls;
    }

    public class CategoryByOperationTypeLocation implements Callable<Category>
    {
        private String operation;
        private String typology;
        private String location;

        public CategoryByOperationTypeLocation(String operation, String typology, String location)
        {
            this.operation = operation;
            this.typology = typology;
            this.location = location;
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
            driver.navigate().to("https://www.idealista.com/en/");
            StartPage startPage = new StartPage(driver);
            startPage.selectOperation(operation);
            if (!startPage.getAvailableTypologies().contains(typology))
            {
                LOGGER.error("Specified Operation + Type combo is not available: {} + {}", operation, typology);
                return null;
            }
            selectOptionsAndStartSearch(startPage, operation, typology, location);
            if (proxyMonitor.ifVerificationAppered(driver))
            {
                driver = proxyMonitor.restartDriver(webDriverProvider);
                driver.navigate().to("https://www.idealista.com/en/");
                selectOptionsAndStartSearch(startPage, operation, typology, location);
            }
            MapPage mapPage = new MapPage(driver);
            mapPage.clickShowAll();
            String categoryUrl = driver.getCurrentUrl();
            LOGGER.info("Found new category url: {}", categoryUrl);
            mapPage.clickIdealistaLink();
            if (proxyMonitor.ifVerificationAppered(driver))
            {
                driver = proxyMonitor.restartDriver(webDriverProvider);
                driver.navigate().to("https://www.idealista.com/");
            }
            return new Category(new URL(categoryUrl), location, operation, typology);
        }
    }

    public Set<String> getCategoriesUrlsByOperationAndTypology(String operationName, String typologyName)
    {
        Set<String> categoriesUrls = new HashSet<>();
        WebDriver driver = webDriverProvider.get();
        StartPage startPage = new StartPage(driver);
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
                driver = proxyMonitor.restartDriver(webDriverProvider);
                driver.navigate().to("https://www.idealista.com/en/");
                continue;
            }
            MapPage mapPage = new MapPage(driver);
            mapPage.clickShowAll();
            String currentUrl = driver.getCurrentUrl();
            categoriesUrls.add(currentUrl);
            LOGGER.info("Added new category url: {}", currentUrl);
            mapPage.clickIdealistaLink();
            if (proxyMonitor.ifVerificationAppered(driver))
            {
                driver = proxyMonitor.restartDriver(webDriverProvider);
                driver.navigate().to("https://www.idealista.com/");
            }
        }
        return categoriesUrls;
    }

    public Set<String> getCategoryUrlByOperationTypologyAndLocation(String operationName, String typologyName,
            String locationName)
    {
        Set<String> categoriesUrls = new HashSet<>();
        WebDriver driver = webDriverProvider.get();
        StartPage startPage = new StartPage(driver);
        startPage.selectOperation(operationName);
        if (!startPage.getAvailableTypologies().contains(typologyName))
        {
            LOGGER.error("Specified Operation + Type combo is not available: {} + {}", operationName, typologyName);
            return Collections.emptySet();
        }
        selectOptionsAndStartSearch(startPage, operationName, typologyName, locationName);
        if (proxyMonitor.ifVerificationAppered(driver))
        {
            driver = proxyMonitor.restartDriver(webDriverProvider);
            driver.navigate().to("https://www.idealista.com/en/");
            selectOptionsAndStartSearch(startPage, operationName, typologyName, locationName);
        }
        MapPage mapPage = new MapPage(driver);
        mapPage.clickShowAll();
        String currentUrl = driver.getCurrentUrl();
        LOGGER.info("Added new category url: {}", currentUrl);
        mapPage.clickIdealistaLink();
        if (proxyMonitor.ifVerificationAppered(driver))
        {
            driver = proxyMonitor.restartDriver(webDriverProvider);
            driver.navigate().to("https://www.idealista.com/");
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
