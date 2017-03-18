package com.idealista.scraper.scraping.advextractor;

import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.ui.page.advertisement.VibboAdvertisementPage;
import com.idealista.scraper.webdriver.INavigateActions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public class VibboAdvertisementExtractor implements IAdvertisementExtractor
{
    private static final Logger LOGGER = LogManager.getLogger(VibboAdvertisementExtractor.class);

    private INavigateActions navigateActions;
    private Category category;
    
    @Override
    public Advertisement call() throws Exception
    {
        URL url = category.getUrl();
        LOGGER.info("Scrapping the page: {}", url);
        WebDriver driver = navigateActions.get(url);
        VibboAdvertisementPage page = new VibboAdvertisementPage();
        page.setWebDriver(driver);
        Advertisement ad = new Advertisement(url, page.getTitle());
        ad.setType(page.getType());
        ad.setSubType(page.getSubType());
        ad.setProvince(page.getProvince());
        ad.setDateOfListing(page.getListingDate());
        ad.setNumberOfViews(page.getNumberOfViews());
        ad.setAddress(page.getAddress());
        ad.setState(page.getState());
        ad.setCity(page.getCity());
        ad.setPostalCode(page.getPostalCode());
        ad.setAge(page.getAge());
        ad.setDescription(page.getDescription());
        ad.setBedRooms(page.getBedrooms());
        ad.setBathRooms(page.getBathrooms());
        ad.setSize(page.getSize());
        ad.setPrice(page.getPrice());
        ad.setEnergyCertification(page.getEnergyCertification());
        ad.setProfessional(page.getProfessional());
        ad.setAgent(page.getListingAgent());
        ad.setAgentPhone(page.getAgentPhone());
        ad.setAgentEmail(page.getAgentEmail());
        ad.setHasImages(page.hasImages());
        ad.setTags(page.getTags());
        return ad;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }

    public void setNavigateActions(INavigateActions navigateActions)
    {
        this.navigateActions = navigateActions;
    }
}
