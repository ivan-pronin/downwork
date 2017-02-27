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
    private String subType;

    public AdvertismentExtractor(WebDriverProvider webDriverProvider, URL pageUrl)
    {
        this.webDriverProvider = webDriverProvider;
        this.pageUrl = pageUrl;
    }

    @Override
    public Advertisment call()
    {
        LOGGER.info("Scrapping the page: {}", pageUrl);
        WebDriver driver = webDriverProvider.get();
        driver.navigate().to(pageUrl);
        driver = proxyMonitor.checkForVerificationAndRestartDriver(driver, webDriverProvider);
        AdvertismentPage page = new AdvertismentPage(driver);
        Advertisment ad = new Advertisment(pageUrl, page.getTitle(), type);
        ad.setSubType(subType);
        ad.setDateOfListing(page.getListingDate());
        // number_of_views
        ad.setAddress(page.getAddress());
        ad.setState(state);
        ad.setCity(page.getCity());
        ad.setPostalCode(page.getPostalCode());
        // age
        ad.setDescription(page.getDescription());
        ad.setBedRooms(page.getBedrooms());
        ad.setBathRooms(page.getBathrooms());
        ad.setSize(page.getSize());
        ad.setPrice(page.getPrice());
        ad.setEnergyCertification(page.getEnergyCertification());
        ad.setProfessional(page.getProfessional());
        ad.setAgent(page.getListingAgent());
        ad.setAgentPhone(page.getAgentPhone());
        // agent email
        ad.setHasImages(page.hasImages());
        ad.setTags(page.getTags());
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

    public void setSubType(String subType)
    {
        this.subType = subType;
    }
}
