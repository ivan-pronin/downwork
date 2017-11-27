package com.idealista.scraper.scraping.advextractor;

import java.net.URL;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.ui.page.IAdvertisementPage;
import com.idealista.scraper.ui.page.advertisement.VibboAdvertisementPage;

@Component
public class VibboAdvertisementExtractor extends AbstractAdvertisementExtractor
{
    private static final Logger LOGGER = LogManager.getLogger(VibboAdvertisementExtractor.class);

    @Autowired
    private Supplier<IAdvertisementPage> advertisementPageSupplier;

    @Override
    public Advertisement call() throws Exception
    {
        Category category = getCategory();
        URL url = category.getUrl();
        LOGGER.info("Scrapping the page: {}", url);
        getNavigateActions().get(url);
        VibboAdvertisementPage page = (VibboAdvertisementPage) advertisementPageSupplier.get();
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
}
