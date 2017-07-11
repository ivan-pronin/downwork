package com.idealista.scraper.scraping.category;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.model.search.CategoryChoosingAttribute;
import com.idealista.scraper.model.search.FotocasaSearchAttributes;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.fotocasa.FotocasaStartPage;
import com.idealista.scraper.util.CollectionUtils;
import com.idealista.scraper.util.WebDriverUtils;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

public class FotocasaCategoriesChooser extends AbstractCategoriesChooser implements ICategoriesChooser
{
    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.FOTOCASA;
    private static final Logger LOGGER = LogManager.getLogger(FotocasaCategoriesChooser.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private CollectionUtils collectionUtils;

    @Override
    public Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext)
    {
        SearchAttributes searchAttributes = searchFilterContext.getSearchAttributes();
        FilterAttributes filterAttributes = searchFilterContext.getFilterAttributes();

        Set<String> userOperations = collectionUtils.wrapWithIdentificationFlag(searchAttributes.getOperations(),
                FotocasaSearchAttributes.OPERATION);
        Set<String> userDistricts = collectionUtils.wrapWithIdentificationFlag(filterAttributes.getDisctricts(),
                FotocasaSearchAttributes.DISTRICT);
        Set<Set<String>> sets = new HashSet<>();
        sets.add(userOperations);
        sets.add(userDistricts);

        Set<CategoryChoosingAttribute> categoryAttributes = collectionUtils.getAllItemCombinations(sets);
        categoryAttributes.forEach(e ->
        {
            e.setNewHomes(filterAttributes.isNewHomes());
            e.setSearchString(searchAttributes.getSearchString());
        });

        Queue<Callable<Set<Category>>> tasks = new ConcurrentLinkedQueue<>();
        for (CategoryChoosingAttribute attribute : categoryAttributes)
        {
            tasks.add(new CategoriesChooser(attribute));
        }
        return executeAndGetResultsForSet(tasks);
    }

    private class CategoriesChooser implements Callable<Set<Category>>
    {
        private String operation;
        private String searchString;
        private boolean newHomes;
        private String district;

        public CategoriesChooser(CategoryChoosingAttribute attribute)
        {
            operation = attribute.getOperation();
            searchString = attribute.getSearchString();
            newHomes = attribute.isNewHomes();
            district = attribute.getDistrict();
        }

        @Override
        public Set<Category> call() throws Exception
        {
            WebDriver driver = webDriverProvider.get();
            driver.navigate().to(SCRAP_TARGET.getMainPageUrl());
            FotocasaStartPage startPage = new FotocasaStartPage();
            startPage.setWebDriver(driver);
            startPage.selectOptionsAndStartSearch(operation, searchString, newHomes);
            WebDriverUtils.waitForAllContentToLoad(driver);

            if (StringUtils.isEmpty(district))
            {
                String categoryUrl = driver.getCurrentUrl();
                LOGGER.info("Found new category url: {}", categoryUrl);
                return new HashSet<>(Arrays.asList(new Category(new URL(categoryUrl), null, operation, null)));
            }
            startPage.selectDistrict(district);
            WebDriverUtils.waitForAllContentToLoad(driver);

            return findAllSubDistricts(startPage);
        }

        private Set<Category> findAllSubDistricts(FotocasaStartPage startPage)
        {
            startPage.selectDistrict(district);
            Set<String> availableSubDistrics = startPage.getAvailableSubDistricts();
            Set<Category> results = new HashSet<>();
            String url = webDriverProvider.get().getCurrentUrl();
            for (String subDistrict : availableSubDistrics)
            {
                try
                {
                    results.add(new SubDistrictChooser(subDistrict, url).call());
                }
                catch (Exception e)
                {
                    LOGGER.error("Error while processing subDistrict: {}, error is: {}", subDistrict, e);
                }
            }
            return results;
        }

        private class SubDistrictChooser implements Callable<Category>
        {
            private String subDistrict;
            private String disctrictUrl;

            private SubDistrictChooser(String subDistrict, String disctrictUrl)
            {
                this.subDistrict = subDistrict;
                this.disctrictUrl = disctrictUrl;
            }

            @Override
            public Category call() throws Exception
            {
                FotocasaStartPage startPage = new FotocasaStartPage();
                WebDriver driver = webDriverProvider.get();
                if (!driver.getCurrentUrl().contains("fotocasa.es"))
                {
                    driver = navigateActions.get(disctrictUrl);
                }
                startPage.setWebDriver(driver);

                startPage.selectDistrict(district);
                WebDriverUtils.waitForAllContentToLoad(driver);

                startPage.selectSubDistrict(subDistrict);
                WebDriverUtils.waitForAllContentToLoad(driver);
                Category category = new Category();
                category.setUrl(new URL(driver.getCurrentUrl()));
                category.setDistrict(district);
                category.setSubDistrict(subDistrict);
                return category;
            }
        }
    }
}
