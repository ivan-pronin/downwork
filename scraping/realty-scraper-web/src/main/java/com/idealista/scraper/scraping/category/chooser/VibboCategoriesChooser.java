package com.idealista.scraper.scraping.category.chooser;

import java.net.URL;
import java.util.Collections;
import java.util.Set;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.search.CategoryChoosingAttribute;
import com.idealista.scraper.scraping.category.provider.VibboCategoriesProvider;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.IStartPage;
import com.idealista.scraper.ui.page.vibbo.VibboStartPage;
import com.idealista.scraper.util.WebDriverUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;

public class VibboCategoriesChooser implements ICategoriesChooser
{
    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.VIBBO;
    private static final Logger LOGGER = LogManager.getLogger(VibboCategoriesProvider.class);

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private Supplier<IStartPage> startPageSupplier;

    private CategoryChoosingAttribute attribute;

    public VibboCategoriesChooser()
    {
    }

    @Override
    public Set<Category> call() throws Exception
    {
        String categoryUrl = null;
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(SCRAP_TARGET.getMainPageUrl());
        VibboStartPage startPage = (VibboStartPage) startPageSupplier.get();
        startPage.selectOptionsAndStartSearch(attribute);
        WebDriverUtils.waitForJSToLoad(driver);
        categoryUrl = driver.getCurrentUrl();
        LOGGER.info("Found new category url: {}", categoryUrl);
        return Collections
                .singleton(new Category(new URL(categoryUrl), null, attribute.getOperation(), attribute.getTypology()));
    }

    public void setAttribute(CategoryChoosingAttribute attribute)
    {
        this.attribute = attribute;
    }
}
