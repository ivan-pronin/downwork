package com.idealista.scraper.page;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.FotocasaStartPage;
import com.idealista.scraper.ui.page.IdealistaStartPage;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class StartPageTests
{

    @Autowired
    private WebDriverProvider webDriverProvider;
    private static final Logger LOGGER = LogManager.getLogger(StartPageTests.class);
    
//    private WebDriverProvider webDriverProvider = new WebDriverProvider();

    @Test
    public void testSelectAction() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(ScrapTarget.FOTOCASA.getMainPageUrl());
        FotocasaStartPage startPage = new FotocasaStartPage();
        startPage.setWebDriver(driver);
        startPage.selectOptionsAndStartSearch("Alquiler", "Barcelona", true);
    }
}
