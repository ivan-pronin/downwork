package com.idealista.scraper.page;

import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.model.RealtyType;
import com.idealista.scraper.search.Category;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.NavigateActions;
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
    private Category category;

    public AdvertismentExtractor(WebDriverProvider webDriverProvider, Category category)
    {
        this.webDriverProvider = webDriverProvider;
        this.category = category;
    }

    @Override
    public Advertisment call()
    {
        URL url = category.getUrl();
        LOGGER.info("Scrapping the page: {}", url);
        WebDriver driver = webDriverProvider.get();
        INavigateActions navigateActions = new NavigateActions(webDriverProvider);
        driver = navigateActions.get(url);
        AdvertismentPage page = new AdvertismentPage(driver);
        Advertisment ad = new Advertisment(url, page.getTitle(), category.getType());
        ad.setSubType(RealtyType.fromString(category.getSubType()));
        ad.setDateOfListing(page.getListingDate());
        // number_of_views
        ad.setAddress(page.getAddress());
        ad.setState(category.getState());
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
}
