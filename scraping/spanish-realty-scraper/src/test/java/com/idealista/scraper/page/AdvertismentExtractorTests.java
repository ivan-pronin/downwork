package com.idealista.scraper.page;

import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.model.RealtyType;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.Callable;

public class AdvertismentExtractorTests
{
    
    private static final Logger LOGGER = LogManager.getLogger(AdvertismentExtractorTests.class);

    //Test
    public static void main(String ... args) throws MalformedURLException
    {
        LOGGER.info("Test started");
        long startTime = System.currentTimeMillis();
        URL url1 = new URL("https://www.idealista.com/en/inmueble/35795178/");
        URL url2 = new URL("https://www.idealista.com/en/inmueble/34168360/");
        WebDriverProvider webDriverProvider = new WebDriverProvider();
//        AdvertismentExtractor extractor1 = new AdvertismentExtractor(webDriverProvider, url1);
//        AdvertismentExtractor extractor2 = new AdvertismentExtractor(webDriverProvider, url2);
//        extractor1.call();
//        extractor2.call();
        printExecutionTime(startTime);
        //webDriverProvider.destroy();
    }

    @Test
    public void testExtractorPErformanceDefaultDriver() throws MalformedURLException
    {
        LOGGER.info("Test started");
        long startTime = System.currentTimeMillis();
        URL url1 = new URL("https://www.idealista.com/en/inmueble/35795178/");
        URL url2 = new URL("https://www.idealista.com/en/inmueble/34168360/");
        WebDriver driver = new ChromeDriver();
        TestAdvExtractor extractor1 = new TestAdvExtractor(driver, url1);
        TestAdvExtractor extractor2 = new TestAdvExtractor(driver, url2);
        extractor1.call();
        extractor2.call();
        printExecutionTime(startTime);
        driver.quit();
    }

    private static void printExecutionTime(long startTime)
    {
        long endTime = System.currentTimeMillis();
        int seconds = (int) ((endTime - startTime) / 1000);
        LOGGER.info("Test ended! Total time taken: " + seconds + " seconds.");
    }

    private class TestAdvExtractor implements Callable<Advertisment>
    {
        private WebDriver driver;
        private URL pageUrl;

        public TestAdvExtractor(WebDriver webDriver, URL pageUrl)
        {
            driver = webDriver;
            this.pageUrl = pageUrl;
        }

        @Override
        public Advertisment call()
        {
            driver.navigate().to(pageUrl);
            AdvertismentPage page = new AdvertismentPage(driver);
            Advertisment ad = new Advertisment(pageUrl, page.getTitle(), "RealtyType.BUILDING");
            ad.setAddress(page.getAddress());
            ad.setDateOfListing(page.getListingDate());
            ad.setState("state");
            ad.setPostalCode(page.getPostalCode());
            ad.setDescription(page.getDescription());
            ad.setBedRooms(page.getBedrooms());
            ad.setBathRooms(page.getBathrooms());
            ad.setSize(page.getSize());
            ad.setAgent(page.getListingAgent());
            ad.setHasImages(page.hasImages());
            return ad;
        }
    }
}
