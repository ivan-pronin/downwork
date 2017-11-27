package com.idealista.scraper;

import java.util.List;
import java.util.function.Supplier;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idealista.scraper.data.IDataTypeService;
import com.idealista.scraper.data.xls.XlsExporter;
import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.scraping.category.FoundUrlsManager;
import com.idealista.scraper.scraping.category.provider.ICategoriesProvider;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.scraping.searchpage.processor.ISeachPageProcessor;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

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
    private Supplier<ISeachPageProcessor> seachPageProcessorSupplier;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private ICategoriesProvider categoriesProvider;

    @Autowired
    private ExecutorServiceProvider executorServiceProvider;

    @Value("#{ T(java.util.Arrays).asList(${proxySources}) }")
    private List<Integer> proxies;

    @Test
    public void testName3() throws Exception
    {
        System.out.println("Proxies: " + proxies.size() + " values: " + proxies);
    }

}
