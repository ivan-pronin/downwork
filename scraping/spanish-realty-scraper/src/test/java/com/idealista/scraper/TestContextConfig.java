package com.idealista.scraper;

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

    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.FOTOCASA;

    //@Test
    public void testClear() throws Exception
    {
        navigateActions.get(new URL(SCRAP_TARGET.getMainPageUrl()));
        FotocasaStartPage page = new FotocasaStartPage();
        WebDriver driver = webDriverProvider.get();
        page.setWebDriver(driver);
        page.selectOptionsAndStartSearch("Venta", "Madrid capital", false);
        
        navigateActions.get(new URL(SCRAP_TARGET.getMainPageUrl()));
        page.selectOptionsAndStartSearch("Venta", "Madrid capital", false);
    }
    
    @Test
    public void testNameChooser() throws Exception
    {
        navigateActions.get(new URL(SCRAP_TARGET.getMainPageUrl()));
        SearchAttributes searchAttributes = new SearchAttributes();
        searchAttributes.setOperations(new HashSet<>(Arrays.asList("Venta")));
        searchAttributes.setSearchString("Madrid Capital");
        FilterAttributes filterAttributes = new FilterAttributes();
        filterAttributes.setNewHomes(false);
//        filterAttributes.setDisctricts(new HashSet<>(Arrays.asList("Centro", "Villaverde", "Carabanchel")));
        filterAttributes.setDisctricts(new HashSet<>(Arrays.asList("Chamartín")));
        GenericSearchFilterContext context = new GenericSearchFilterContext();
        context.setSearchAttributes(searchAttributes);
        context.setFilterAttributes(filterAttributes);

        Set<Category> categoriesBaseUrls = categoriesChooser.getCategoriesUrls(context);
        ExecutorService executor = executorServiceProvider.getExecutor();
        executor.shutdown();
        executor.awaitTermination(3, TimeUnit.MINUTES);
        System.out.println("FOUND CATEGORIES: " + categoriesBaseUrls.size());
        categoriesBaseUrls.forEach(System.out::println);
        webDriverProvider.destroy();
    }

    // @Test
    public void testAds() throws Exception
    {
        String url = "http://www.fotocasa.es/vivienda/boadilla-del-monte/calefaccion-ascensor-del-arco-139057655"
                + "?RowGrid=6&tti=1&opi=300&RowGrid=6&tti=1&opi=300";
        Category c = new Category(new URL(url), null);
        IAdvertisementExtractor ads = adsFactory.create(c);
        Advertisement a = ads.call();
        System.out.println("");
        String jsonObj = "\"{\"abc\":\"efg\"}\"";
        WebDriver driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(1, TimeUnit.SECONDS);
    }

    // @Test
    public void testName() throws Exception
    {
        Category c = new Category(
                new URL("http://www.fotocasa.es/es/comprar/casas/madrid-provincia/todas-las-zonas/l?latitude=40.415&longitude=-3.7104&combinedLocationIds=724,14,28,0,0,0,0,0,0"),
                null);
        FotocasaSearchPageProcessor processor = (FotocasaSearchPageProcessor) factory.create(c);
        Set<Category> adUrls = processor.call();
        System.out.println("... 1st Category ...");
        System.out.println("FOUND urls : " + adUrls.size());
        adUrls.forEach(System.out::println);

        Category c2 = new Category(
                new URL("http://www.fotocasa.es/search/results.aspx?opi=36&ts=&t=&llm=724,0,0,0,0,0,0,0,0&bti=2&bsm=&tti=3&mode=1&prchti=1&cu=es-es&minp=&maxp=&mins=&maxs=&minr=&minb=&esm=&csm="),
                null);
        FotocasaSearchPageProcessor processor2 = (FotocasaSearchPageProcessor) factory.create(c2);
        Set<Category> adUrls2 = processor2.call();
        System.out.println("... 2nd Category ...");
        System.out.println("FOUND urls : " + adUrls2.size());
        adUrls2.forEach(System.out::println);

        webDriverProvider.destroy();
    }
}
