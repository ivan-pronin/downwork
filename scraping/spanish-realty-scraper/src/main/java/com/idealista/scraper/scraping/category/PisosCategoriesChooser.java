package com.idealista.scraper.scraping.category;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.service.PisosScrappingService;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.pisos.PisosMapPage;
import com.idealista.scraper.ui.page.pisos.PisosSearchPage;
import com.idealista.scraper.ui.page.pisos.PisosStartPage;
import com.idealista.scraper.util.WebDriverUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

public class PisosCategoriesChooser extends AbstractCategoriesChooser implements ICategoriesChooser
{
    private static final Logger LOGGER = LogManager.getLogger(PisosCategoriesChooser.class);

    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.PISOS;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private ProxyMonitor proxyMonitor;

    @Override
    public Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext)
    {
        SearchAttributes searchAttributes = searchFilterContext.getSearchAttributes();
        List<Map<String, List<String>>> filterAttributes = searchFilterContext.getGenericFilterAttributes();

        Set<String> userOperations = searchAttributes.getOperations();
        Set<String> userTypologies = searchAttributes.getTypologies();
        Set<String> userLocations = searchAttributes.getLocations();
        PisosStartPage startPage = new PisosStartPage();
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
                            for (Map<String, List<String>> filterSet : filterAttributes)
                            {
                                for (String district : filterSet.get("distro"))
                                {
                                    results.add(new CategoryBySearchAndFilterAttributes(operation, typology, location,
                                            filterSet.get("zone").get(0), filterSet.get("municipio").get(0), district,
                                            filterSet.get("extras").get(0)));
                                }
                            }
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

    private void selectOptionsAndStartSearch(PisosStartPage startPage, String operationName, String typologyName,
            String location)
    {
        startPage.selectOperation(operationName);
        startPage.selectTypology(typologyName);
        startPage.selectLocation(location);
        startPage.clickSearch();
    }

    private class CategoryBySearchAndFilterAttributes implements Callable<Category>
    {
        private String operation;
        private String typology;
        private String location;
        private String zone;
        private String municipio;
        private String district;
        private String extras;

        public CategoryBySearchAndFilterAttributes(String operation, String typology, String location, String zone,
                String municipio, String district, String extras)
        {
            this.operation = operation;
            this.typology = typology;
            this.location = location;
            this.zone = zone;
            this.municipio = municipio;
            this.district = district;
            this.extras = extras;
        }

        @Override
        public Category call() throws Exception
        {
            WebDriver driver = webDriverProvider.get();
            driver.navigate().to(getMainPageUrl());
            PisosStartPage startPage = new PisosStartPage();
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
                driver.navigate().to(getMainPageUrl());
                selectOptionsAndStartSearch(startPage, operation, typology, location);
            }
            PisosMapPage mapPage = new PisosMapPage();
            mapPage.setWebDriver(driver);
            mapPage.clickShowAll();

            return applyFilter();
        }

        private Category applyFilter() throws MalformedURLException
        {
            PisosSearchPage page = new PisosSearchPage();
            WebDriver driver = webDriverProvider.get();
            page.setWebDriver(driver);
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
            return SCRAP_TARGET.getMainPageUrl();
        }
    }
}
