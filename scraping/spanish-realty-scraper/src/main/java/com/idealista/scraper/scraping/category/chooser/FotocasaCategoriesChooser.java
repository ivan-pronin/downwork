package com.idealista.scraper.scraping.category.chooser;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.search.CategoryChoosingAttribute;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.IStartPage;
import com.idealista.scraper.ui.page.fotocasa.FotocasaStartPage;
import com.idealista.scraper.util.WebDriverUtils;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

public class FotocasaCategoriesChooser implements ICategoriesChooser
{
    private static final Logger LOGGER = LogManager.getLogger(FotocasaCategoriesChooser.class);

    private String district;
    private CategoryChoosingAttribute attribute;

    @Autowired
    private Supplier<FotocasaSubDistrictChooser> subDistrictChooser;

    @Autowired
    private Supplier<IStartPage> startPageSupplier;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private WebDriverProvider webDriverProvider;

    public FotocasaCategoriesChooser()
    {
    }

    @Override
    public Set<Category> call() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        navigateActions.get(ScrapTarget.FOTOCASA.getMainPageUrl());
        FotocasaStartPage startPage = (FotocasaStartPage) startPageSupplier.get();
        String operation = attribute.getOperation();
        startPage.selectOptionsAndStartSearch(operation, attribute.getSearchString(), attribute.isNewHomes());
        WebDriverUtils.waitForAllContentToLoad(driver);
        district = attribute.getDistrict();
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
                FotocasaSubDistrictChooser fotocasaSubDistrictChooser = subDistrictChooser.get();
                fotocasaSubDistrictChooser.setDisctrictUrl(url);
                fotocasaSubDistrictChooser.setDistrict(district);
                fotocasaSubDistrictChooser.setSubDistrict(subDistrict);
                results.add(fotocasaSubDistrictChooser.call());
            }
            catch (Exception e)
            {
                LOGGER.error("Error while processing subDistrict: {}, error is: {}", subDistrict, e);
            }
        }
        return results;
    }

    public void setAttribute(CategoryChoosingAttribute attribute)
    {
        this.attribute = attribute;
    }
}
