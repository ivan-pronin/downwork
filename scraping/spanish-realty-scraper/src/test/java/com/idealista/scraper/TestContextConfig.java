package com.idealista.scraper;

import static org.junit.Assert.*;

import com.idealista.scraper.data.DataType;
import com.idealista.scraper.data.IDataTypeService;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.ui.page.advertisement.VibboAdvertisementPage;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = AppConfig.class)
@TestPropertySource("file:settings/test.properties")
public class TestContextConfig
{
    @Value("${set1}")
    private Set<String> set1;

    @Value("${set2}")
    private Set<String> set2;

    @Value("${set3}")
    private Set<String> set3;

    @Value("${testEnum1}")
    private String testValue;

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
    
    //@Test
    public void testName5() throws Exception
    {
        String newAdsFileName = dataTypeService.getNewAdsFileName();
        System.out.println(newAdsFileName);
        System.out.println(DataType.fromString(newAdsFileName));
        String processedAdsFileName = dataTypeService.getProcessedAdsFileName();
        System.out.println(processedAdsFileName);
        System.out.println(DataType.fromString(processedAdsFileName));
    }
    
    //@Test
    public void testName3() throws Exception
    {
        String url1 = "http://www.vibbo.com/alquiler-de-casas-y-chales-toda-espana-profesionales/?ca=0_s&fPos=346&fOn=sb_f_input";
        String url2 = "http://www.vibbo.com/alquiler-de-casas-rurales-toda-espana-profesionales/?ca=0_s&fPos=638&fOn=sb_f_input";
        String url3 = "http://www.vibbo.com/alquiler-de-garajes-y-trasteros-toda-espana/?ca=0_s&ss=190&se=270&fPos=368&fOn=sb_se";
        String url4 = "http://www.vibbo.com/alquiler-de-garajes-y-trasteros-toda-espana/?ca=0_s&ss=190&fPos=355&fOn=sb_ss";
        
        Set<String> cats = new HashSet<>(Arrays.asList(url1,url2,url3,url4));
        
        for (String s : cats)
        {
            Category cat = new Category();
            cat.setUrl(new URL(s));
            paginator.getAllPageUrls(cat);
        }
        webDriverProvider.destroy();
    }

    @Test
    public void testName7() throws Exception
    {
        WebDriver driver = webDriverProvider.get();
        driver.get("http://www.vibbo.com/madrid/chalet-torre-en-rozas-centro-el-cano-maraca/a100501003/?ca=28_s&st=a&c=59");
        VibboAdvertisementPage page = new VibboAdvertisementPage();
        page.setWebDriver(driver);
        
        String typeText = page.getType();
        String[] parts = typeText.split(" > ");
        System.out.println(typeText);
    }
    //@Test
    public void testName2() throws Exception
    {
        scrappingService.scrapSite();
    }

    // @Test
    public void testName() throws Exception
    {
        System.out.println(set1);
        System.out.println(set2);
        System.out.println(set3);
        System.out.println(testValue);
    }
}
