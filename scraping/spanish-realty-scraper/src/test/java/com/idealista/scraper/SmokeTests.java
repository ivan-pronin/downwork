package com.idealista.scraper;

import com.idealista.scraper.page.AdvertismentExtractor;
import com.idealista.scraper.page.Paginator;
import com.idealista.scraper.page.SearchPageProcessor;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.junit.Test;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class SmokeTests
{
    @Test
    public void testGrabOneAdvertismentIdealista()
    {
        String baseUrl = "https://www.idealista.com/venta-viviendas/a-coruna-provincia/";
        WebDriverProvider webDriverProvider = new WebDriverProvider();
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(baseUrl);
        Paginator paginator = new Paginator();
        Queue<URL> pagesToProcess = new ConcurrentLinkedQueue<>();
        pagesToProcess.addAll(paginator.getAllPageUrls(driver, baseUrl));

        URL randomPage = pagesToProcess.iterator().next();
        System.out.println("Random page is: " + randomPage);
        SearchPageProcessor searchPageProcessor = new SearchPageProcessor(webDriverProvider, randomPage);
        Queue<URL> adUrls = new ConcurrentLinkedQueue<>();
        adUrls.addAll(searchPageProcessor.call());
        System.out.println(adUrls);

        URL randomAdvUrl = adUrls.iterator().next();
        System.out.println("Random Adv url: " + randomAdvUrl);
        AdvertismentExtractor advPage = new AdvertismentExtractor(webDriverProvider, randomAdvUrl);
        System.out.println(advPage.call());
    }
}
