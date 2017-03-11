package com.idealista.scraper.scraping;

import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.RealtyType;
import com.idealista.scraper.ui.page.AdvertismentPage;
import com.idealista.scraper.webdriver.INavigateActions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.net.URL;
import java.util.concurrent.Callable;

public final class AdvertismentExtractor implements Callable<Advertisment>
{
    private static final Logger LOGGER = LogManager.getLogger(AdvertismentExtractor.class);

    private INavigateActions navigateActions;
    private Category category;
    private AdvertismentPage page;

    public AdvertismentExtractor(Category category,
            INavigateActions navigateActions, AdvertismentPage page)
    {
        this.category = category;
        this.navigateActions = navigateActions;
        this.page = page;
    }

    @Override
    public Advertisment call()
    {
        URL url = category.getUrl();
        LOGGER.info("Scrapping the page: {}", url);
        WebDriver driver = navigateActions.get(url);
        page.setWebDriver(driver);
        Advertisment ad = new Advertisment(url, page.getTitle(), category.getType());
        ad.setSubType(RealtyType.fromString(category.getSubType()));
        ad.setProvince(category.getProvince());
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
