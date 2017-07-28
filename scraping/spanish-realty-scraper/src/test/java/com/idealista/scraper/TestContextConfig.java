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
import com.idealista.scraper.scraping.category.chooser.ICategoriesChooser;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.scraping.searchpage.factory.ISearchPageProcessorFactory;
import com.idealista.scraper.scraping.searchpage.processor.FotocasaSearchPageProcessor;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.ui.page.fotocasa.FotocasaStartPage;
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
    
    @Value("#{ T(org.springframework.util.StringUtils).delimitedListToStringArray('${distros}',';') }")
    private List<String> distros;
    
    @Value("#{ T(org.springframework.util.StringUtils).delimitedListToStringArray('${distros2}',';') }")
    private List<String> distros2;
    
    @Value("#{ T(org.springframework.util.StringUtils).delimitedListToStringArray('${distros3}',';') }")
    private List<String> distros3;
    
    @Value("#{ T(java.util.Arrays).asList('${zone}') }")
    private List<String> zone;

    @Value("#{ T(java.util.Arrays).asList('${municipio}') }")
    private List<String> municipio;

    @Test
    public void testName3() throws Exception
    {
        System.out.println("Distros: " + distros.size() + " values: " + distros);
        System.out.println("Distros2: " + distros2.size() + " values: " + distros2);
        System.out.println("Distros3: " + distros3.size() + " values: " + distros3);
        System.out.println("Zone: " + zone.size() + " values: " + zone);
        System.out.println("Municipio: " + municipio.size() + " values: " + municipio);
    }

}
