package com.idealista.scraper.scraping.category.chooser;

import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.scraping.category.provider.IdealistaCategoriesProvider;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.IMapPage;
import com.idealista.scraper.ui.page.ISearchPage;
import com.idealista.scraper.ui.page.IStartPage;
import com.idealista.scraper.ui.page.idealista.IdealistaMapPage;
import com.idealista.scraper.ui.page.idealista.IdealistaSearchPage;
import com.idealista.scraper.ui.page.idealista.IdealistaStartPage;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

public class IdealistaCategoriesChooser implements ICategoriesChooser
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaCategoriesProvider.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private ProxyMonitor proxyMonitor;

    @Autowired
    private Supplier<IStartPage> startPageSupplier;

    @Autowired
    private Supplier<ISearchPage> searchPageSupplier;

    @Autowired
    private Supplier<IMapPage> mapPageSupplier;

    private String operation;
    private String typology;
    private String location;
    private String province;
    private FilterAttributes filterAttributes;

    public IdealistaCategoriesChooser()
    {
    }

    @Override
    public Set<Category> call() throws Exception
    {
        if ("International".equalsIgnoreCase(location))
        {
            LOGGER.info("Skipping International site by now...");
            return null;
        }
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(getMainPageLocalizedUrl());
        IdealistaStartPage startPage = (IdealistaStartPage) startPageSupplier.get();
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
        IdealistaMapPage mapPage = (IdealistaMapPage) mapPageSupplier.get();
        mapPage.clickShowAll();

        IdealistaSearchPage searchPage = (IdealistaSearchPage) searchPageSupplier.get();
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
        return Collections.singleton(category);
    }

    private String getMainPageLocalizedUrl()
    {
        return ScrapTarget.IDEALISTA.getMainPageLocalizedUrl(appConfig.getLanguage());
    }

    private void selectOptionsAndStartSearch(IdealistaStartPage startPage, String operationName, String typologyName,
            String location)
    {
        startPage.selectOperation(operationName);
        startPage.selectTypology(typologyName);
        startPage.selectLocation(location);
        startPage.clickSearch();
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public void setTypology(String typology)
    {
        this.typology = typology;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public void setFilterAttributes(FilterAttributes filterAttributes)
    {
        this.filterAttributes = filterAttributes;
    }

}
