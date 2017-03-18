package com.idealista.scraper.service;

import com.idealista.scraper.model.Advertisement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;

public interface IScrappingService
{
    void scrapSite() throws InterruptedException, MalformedURLException;

    void setAdvertismentExtractorResults(BlockingQueue<Future<Advertisement>> advertismentExtractorResults);
    
    BlockingQueue<URL> getAdvertismentUrlsInProgress();
}
