package com.idealista.scraper;

import static org.junit.Assert.*;

import com.idealista.scraper.data.IDataTypeService;
import com.idealista.scraper.data.xls.XlsExporter;
import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.scraping.advextractor.IAdvertisementExtractor;
import com.idealista.scraper.scraping.advextractor.IAdvertisementExtractorFactory;
import com.idealista.scraper.scraping.category.FoundUrlsManager;
import com.idealista.scraper.scraping.category.ICategoriesChooser;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.scraping.searchpage.FotocasaSearchPageProcessor;
import com.idealista.scraper.scraping.searchpage.ISearchPageProcessorFactory;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.FotocasaStartPage;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.TimeUnit;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
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

    @Autowired
    private ISearchPageProcessorFactory factory;

    @Autowired
    private IAdvertisementExtractorFactory adsFactory;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private ICategoriesChooser categoriesChooser;

    @Autowired
    private ExecutorServiceProvider executorServiceProvider;

    @Value("#{ T(java.util.Arrays).asList(${proxySources}) }")
    private List<Integer> proxies;

    @Test
    public void testName() throws Exception
    {
        System.out.println(appConfig.getLanguage());
    }

}
