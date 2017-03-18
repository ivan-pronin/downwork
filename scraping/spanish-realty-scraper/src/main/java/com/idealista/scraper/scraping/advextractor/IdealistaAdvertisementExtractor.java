package com.idealista.scraper.scraping.advextractor;

import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.RealtyType;
import com.idealista.scraper.ui.page.advertisement.IdealistaAdvertisementPage;
import com.idealista.scraper.webdriver.INavigateActions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import java.net.URL;

public final class IdealistaAdvertisementExtractor implements IAdvertisementExtractor
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaAdvertisementExtractor.class);

    private INavigateActions navigateActions;
    private Category category;
    private String language;

    @Override
    public Advertisement call()
    {
        URL url = category.getUrl();
        LOGGER.info("Scrapping the page: {}", url);
        WebDriver driver = navigateActions.get(url);
        IdealistaAdvertisementPage page = new IdealistaAdvertisementPage();
        page.setWebDriver(driver);
        page.setLanguage(language);
        Advertisement ad = new Advertisement(url, page.getTitle(), category.getType());
        ad.setSubType(RealtyType.fromString(category.getSubType()).name());
        ad.setProvince(category.getProvince());
        ad.setDateOfListing(page.getListingDate());
        ad.setNumberOfViews(page.getNumberOfViews());
        ad.setAddress(page.getAddress());
        ad.setState(category.getState());
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

    public void setLanguage(String language)
    {
        this.language = language;
    }

    public void setNavigateActions(INavigateActions navigateActions)
    {
        this.navigateActions = navigateActions;
    }
}
