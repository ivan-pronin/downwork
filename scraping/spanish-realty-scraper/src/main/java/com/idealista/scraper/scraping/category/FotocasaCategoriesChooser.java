package com.idealista.scraper.scraping.category;

import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.concurrent.Callable;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.FotocasaStartPage;
import com.idealista.scraper.util.WebDriverUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FotocasaCategoriesChooser extends AbstractCategoriesChooser implements ICategoriesChooser
{
    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.FOTOCASA;
    private static final Logger LOGGER = LogManager.getLogger(FotocasaCategoriesChooser.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Override
    public Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext)
    {
        SearchAttributes searchAttributes = searchFilterContext.getSearchAttributes();
        FilterAttributes filterAttributes = searchFilterContext.getFilterAttributes();

        CategoriesChooser categoryChooser = new CategoriesChooser(searchAttributes.getOperations().iterator().next(),
                searchAttributes.getSearchString(), filterAttributes.isNewHomes());

        try
        {
            return Collections.singleton(categoryChooser.call());
        }
        catch (Exception e)
        {
            LOGGER.error("Error while collecting gategories Urls: {}", e);
            return Collections.emptySet();
        }
    }

    private class CategoriesChooser implements Callable<Category>
    {
        private String operation;
        private String searchString;
        private boolean newHomes;

        private CategoriesChooser(String operation, String searchString, boolean newHomes)
        {
            this.operation = operation;
            this.searchString = searchString;
            this.newHomes = newHomes;
        }

        @Override
        public Category call() throws Exception
        {
            String categoryUrl = null;
            WebDriver driver = webDriverProvider.get();
            driver.navigate().to(SCRAP_TARGET.getMainPageUrl());
            FotocasaStartPage startPage = new FotocasaStartPage();
            startPage.setWebDriver(driver);
            startPage.selectOptionsAndStartSearch(operation, searchString, newHomes);
            WebDriverUtils.waitForAllContentToLoad(driver);
            categoryUrl = driver.getCurrentUrl();
            LOGGER.info("Found new category url: {}", categoryUrl);
            return new Category(new URL(categoryUrl), null, operation, null);
        }
    }
}
