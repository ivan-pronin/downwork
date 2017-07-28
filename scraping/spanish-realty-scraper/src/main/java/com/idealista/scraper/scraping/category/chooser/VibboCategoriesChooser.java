package com.idealista.scraper.scraping.category.chooser;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.search.CategoryChoosingAttribute;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.model.search.VibboSearchAttributes;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.vibbo.VibboStartPage;
import com.idealista.scraper.util.CollectionUtils;
import com.idealista.scraper.util.WebDriverUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

public class VibboCategoriesChooser extends AbstractCategoriesChooser implements ICategoriesChooser
{
    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.VIBBO;
    private static final Logger LOGGER = LogManager.getLogger(VibboCategoriesChooser.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private CollectionUtils collectionUtils;

    @Override
    public Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext)
    {
        SearchAttributes searchAttributes = searchFilterContext.getSearchAttributes();
        Set<String> userTypologies = collectionUtils.wrapWithIdentificationFlag(searchAttributes.getTypologies(),
                VibboSearchAttributes.TYPOLOGY);
        Set<String> userOperations = collectionUtils.wrapWithIdentificationFlag(searchAttributes.getOperations(),
                VibboSearchAttributes.OPERATION);
        Set<String> userPropertyTypes = collectionUtils.wrapWithIdentificationFlag(searchAttributes.getPropertyTypes(),
                VibboSearchAttributes.PROPERTY_TYPE);
        Set<String> advertiser = collectionUtils.wrapWithIdentificationFlag(
                new HashSet<>(Arrays.asList(searchAttributes.getAdvertiser())), VibboSearchAttributes.ADVERTISER);
        Set<Set<String>> sets = new HashSet<>();
        sets.add(advertiser);
        sets.add(userPropertyTypes);
        sets.add(userOperations);
        sets.add(userTypologies);

        Set<CategoryChoosingAttribute> categoryAttributes = collectionUtils.getAllItemCombinations(sets);

        Queue<Callable<Category>> results = new ConcurrentLinkedQueue<>();
        categoryAttributes.forEach(e -> results.add(new CategoriesChooser(e)));

        return executeAndGetResults(results);
    }

    private class CategoriesChooser implements Callable<Category>
    {
        private CategoryChoosingAttribute attribute;

        public CategoriesChooser(CategoryChoosingAttribute attribute)
        {
            this.attribute = attribute;
        }

        @Override
        public Category call() throws Exception
        {
            String categoryUrl = null;
            WebDriver driver = webDriverProvider.get();
            driver.navigate().to(SCRAP_TARGET.getMainPageUrl());
            VibboStartPage startPage = new VibboStartPage();
            startPage.setWebDriver(driver);
            startPage.selectOptionsAndStartSearch(attribute);
            WebDriverUtils.waitForJSToLoad(driver);
            categoryUrl = driver.getCurrentUrl();
            LOGGER.info("Found new category url: {}", categoryUrl);
            return new Category(new URL(categoryUrl), null, attribute.getOperation(), attribute.getTypology());
        }
    }
}
