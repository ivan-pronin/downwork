package com.idealista.scraper.model.search;

import com.idealista.scraper.service.ScrapTarget;

public interface IGenericSearchAttributes
{
    ScrapTarget getScrapTarget();
    
    String getIdentificationFlag();
}
