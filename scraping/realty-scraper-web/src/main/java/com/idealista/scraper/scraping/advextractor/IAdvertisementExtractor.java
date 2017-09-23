package com.idealista.scraper.scraping.advextractor;

import java.util.concurrent.Callable;

import com.idealista.scraper.model.Advertisement;

public interface IAdvertisementExtractor extends Callable<Advertisement>
{

}
