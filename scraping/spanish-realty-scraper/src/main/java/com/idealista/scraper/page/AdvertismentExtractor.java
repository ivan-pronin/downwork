package com.idealista.scraper.page;

import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.model.RealtyType;
import com.idealista.scraper.proxy.ProxyMonitor;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.concurrent.Callable;

public class AdvertismentExtractor implements Callable<Advertisment>
{
    private static final Logger LOGGER = LogManager.getLogger(AdvertismentExtractor.class);

    private WebDriverProvider webDriverProvider;
    private URL pageUrl;
    private ProxyMonitor proxyMonitor = new ProxyMonitor();
    private RealtyType type;
    private String state;

    public AdvertismentExtractor(WebDriverProvider webDriverProvider, URL pageUrl)
    {
        this.webDriverProvider = webDriverProvider;
        this.pageUrl = pageUrl;
    }

    @Override
    public Advertisment call()
    {
        LOGGER.info("AdvertismentExtractor is working at page: {}", pageUrl);
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(pageUrl);
        //synchronized (this)
        //{
         //   driver = proxyMonitor.checkForVerificationAndRestarttDriver(driver, webDriverProvider);
        //}
        AdvertismentPage page = new AdvertismentPage(driver);
        Advertisment ad = new Advertisment(pageUrl, page.getTitle(), type);
        ad.setAddress(page.getAddress());
        ad.setDateOfListing(page.getListingDate());
        ad.setState(state);
        ad.setPostalCode(page.getPostalCode());
        ad.setDescription(page.getDescription());
        ad.setBedRooms(Integer.parseInt(page.getBedrooms()));
        ad.setBathRooms(Integer.parseInt(page.getBathrooms()));
        ad.setSize(page.getSize());
        ad.setAgent(page.getListingAgent());
        ad.setHasImages(page.hasImages());
        return ad;
    }

    public void setType(RealtyType type)
    {
        this.type = type;
    }

    public void setState(String state)
    {
        this.state = state;
    }
}
