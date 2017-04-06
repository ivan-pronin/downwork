package com.idealista.scraper;

import static org.junit.Assert.*;

import com.idealista.scraper.data.IDataTypeService;
import com.idealista.scraper.data.xls.XlsExporter;
import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.advextractor.IAdvertisementExtractor;
import com.idealista.scraper.scraping.advextractor.IAdvertisementExtractorFactory;
import com.idealista.scraper.scraping.category.FoundUrlsManager;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.scraping.searchpage.FotocasaSearchPageProcessor;
import com.idealista.scraper.scraping.searchpage.ISearchPageProcessorFactory;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
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

    @Value("#{ T(java.util.Arrays).asList(${proxySources}) }")
    private List<Integer> proxies;

    @Test
    public void testAds() throws Exception
    {
        String url = "http://www.fotocasa.es/vivienda/boadilla-del-monte/calefaccion-ascensor-del-arco-139057655"
                + "?RowGrid=6&tti=1&opi=300&RowGrid=6&tti=1&opi=300";
        Category c = new Category(new URL(url), null);
        IAdvertisementExtractor ads = adsFactory.create(c);
        Advertisement a = ads.call();
        System.out.println("");
        String jsonObj = "\"{\"abc\":\"efg\"}\"";
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
