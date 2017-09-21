package com.idealista.scraper.scraping.category.chooser;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.category.provider.PisosCategoriesProvider;
import com.idealista.scraper.service.PisosScrappingService;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.IMapPage;
import com.idealista.scraper.ui.page.ISearchPage;
import com.idealista.scraper.ui.page.IStartPage;
import com.idealista.scraper.ui.page.pisos.PisosMapPage;
import com.idealista.scraper.ui.page.pisos.PisosSearchPage;
import com.idealista.scraper.ui.page.pisos.PisosStartPage;
import com.idealista.scraper.util.WebDriverUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

public class PisosCategoriesChooser implements ICategoriesChooser
{
    private static final Logger LOGGER = LogManager.getLogger(PisosCategoriesProvider.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

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
    private String zone;
    private String municipio;
    private String district;
    private String extras;

    public PisosCategoriesChooser()
    {
    }

    @Override
    public Set<Category> call() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(getMainPageUrl());
        PisosStartPage startPage = (PisosStartPage) startPageSupplier.get();
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
            driver.navigate().to(getMainPageUrl());
            selectOptionsAndStartSearch(startPage, operation, typology, location);
        }
        PisosMapPage mapPage = (PisosMapPage) mapPageSupplier.get();
        mapPage.clickShowAll();

        return Collections.singleton(applyFilter());
    }

    private Category applyFilter() throws MalformedURLException
    {
        PisosSearchPage page = (PisosSearchPage) searchPageSupplier.get();
        WebDriver driver = webDriverProvider.get();
        if (!PisosScrappingService.NOT_SPECIFIED.equals(zone))
        {
            page.selectZone(zone);
            page.selectMunicipio(municipio);
            page.selectDistrict(district);
        }
        WebDriverUtils.waitForAllContentToLoad(driver);
        applyExtrasFilter(page);
        WebDriverUtils.waitForAllContentToLoad(driver);
        Category category = new Category(new URL(driver.getCurrentUrl()), location, operation, typology);
        category.setDistrict(district);
        LOGGER.info("Found new category: {}", category);
        return category;
    }

    private void selectOptionsAndStartSearch(PisosStartPage startPage, String operationName, String typologyName,
            String location)
    {
        startPage.selectOperation(operationName);
        startPage.selectTypology(typologyName);
        startPage.selectLocation(location);
        startPage.clickSearch();
    }

    private void applyExtrasFilter(PisosSearchPage page)
    {
        if (StringUtils.isEmpty(extras))
        {
            return;
        }
        page.selectExtras(extras);
    }

    private String getMainPageUrl()
    {
        return ScrapTarget.PISOS.getMainPageUrl();
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

    public void setZone(String zone)
    {
        this.zone = zone;
    }

    public void setMunicipio(String municipio)
    {
        this.municipio = municipio;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public void setExtras(String extras)
    {
        this.extras = extras;
    }
}
