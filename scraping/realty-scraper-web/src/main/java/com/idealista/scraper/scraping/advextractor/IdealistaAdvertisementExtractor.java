package com.idealista.scraper.scraping.advextractor;

import java.net.URL;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.IdealistaRealtyType;
import com.idealista.scraper.ui.page.IAdvertisementPage;
import com.idealista.scraper.ui.page.advertisement.IdealistaAdvertisementPage;

public final class IdealistaAdvertisementExtractor extends AbstractAdvertisementExtractor
{
    private static final Logger LOGGER = LogManager.getLogger(IdealistaAdvertisementExtractor.class);
    private String language;

    @Autowired
    private Supplier<IAdvertisementPage> advertisementPageSupplier;

    @Override
    public Advertisement call()
    {
        Category category = getCategory();
        URL url = category.getUrl();
        LOGGER.info("Scrapping the page: {}", url);
        getNavigateActions().get(url);
        IdealistaAdvertisementPage page = (IdealistaAdvertisementPage) advertisementPageSupplier.get();
        page.setLanguage(language);
        Advertisement ad = new Advertisement(url, page.getTitle(), category.getType());
        ad.setSubType(IdealistaRealtyType.fromString(category.getSubType()).name());
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

    public void setLanguage(String language)
    {
        this.language = language;
    }
}
