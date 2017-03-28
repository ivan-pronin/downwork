package com.idealista.scraper;

import static org.junit.Assert.*;

import com.idealista.scraper.data.DataType;
import com.idealista.scraper.data.IDataTypeService;
import com.idealista.scraper.data.xls.XlsExporter;
import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.category.FoundUrlsManager;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.ui.page.advertisement.IdealistaAdvertisementPage;
import com.idealista.scraper.ui.page.advertisement.VibboAdvertisementPage;
import com.idealista.scraper.util.FileUtils;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfig.class)
// @TestPropertySource("file:settings/test.properties")
public class TestContextConfig
{
    @Autowired
    private IScrappingService scrappingService;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private IPaginator paginator;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private IDataTypeService dataTypeService;

    @Autowired
    private XlsExporter xlsExporter;

    @Autowired
    private FoundUrlsManager foundUrlsManager;

    @Value("#{ T(java.util.Arrays).asList(${proxySources}) }")
    private List<Integer> proxies;

    @Test
    public void testName() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        driver.get("https://www.idealista.com/en/inmueble/31586085/");
        IdealistaAdvertisementPage page = new IdealistaAdvertisementPage();
        page.setWebDriver(driver);

        System.out.println(page.getSize());
    }
}
