package com.idealista.scraper.scraping.category.chooser;

import java.net.URL;
import java.util.concurrent.Callable;
import java.util.function.Supplier;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.ui.page.IStartPage;
import com.idealista.scraper.ui.page.fotocasa.FotocasaStartPage;
import com.idealista.scraper.util.WebDriverUtils;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

public class FotocasaSubDistrictChooser implements Callable<Category>
{
    private String subDistrict;
    private String disctrictUrl;
    private String district;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private Supplier<IStartPage> startPageSupplier;

    public FotocasaSubDistrictChooser()
    {
    }

    @Override
    public Category call() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        if (!driver.getCurrentUrl().contains("fotocasa.es"))
        {
            driver = navigateActions.get(disctrictUrl);
        }
        FotocasaStartPage startPage = (FotocasaStartPage) startPageSupplier.get();

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

    public void setSubDistrict(String subDistrict)
    {
        this.subDistrict = subDistrict;
    }

    public void setDisctrictUrl(String disctrictUrl)
    {
        this.disctrictUrl = disctrictUrl;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }
}
