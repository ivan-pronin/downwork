package com.idealista.scraper.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

import org.apache.logging.log4j.Logger;

import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.model.search.SearchAttributes;

public interface IScrappingService
{
    void scrapSite() throws InterruptedException, MalformedURLException;

    void setAdvertismentExtractorResults(BlockingQueue<Future<Advertisement>> advertismentExtractorResults);

    BlockingQueue<URL> getAdvertismentUrlsInProgress();

    static void printAttributesInfo(Logger logger, SearchAttributes searchAttributes, FilterAttributes filterAttributes)
    {
        logger.info(" === === Printing parsed SearchAttributes === === ");
        logger.info(searchAttributes);
        logger.info(" === === Printing parsed FilterAttributes === === ");
        logger.info(filterAttributes);
        logger.info(" === === === === === === ===  === === === === === ");
        logger.info("");
    }
}
