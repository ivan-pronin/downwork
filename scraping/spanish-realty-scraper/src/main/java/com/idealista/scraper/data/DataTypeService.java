package com.idealista.scraper.data;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class DataTypeService implements IDataTypeService
{
    @Value("./data/${scrapTarget}/newAds.txt")
    private String newAdsFileName;
    
    @Value("./data/${scrapTarget}/processedAds.txt")
    private String processedAdsFileName;
    
    @Override
    public String getNewAdsFileName()
    {
        return newAdsFileName;
    }

    @Override
    public String getProcessedAdsFileName()
    {
        return processedAdsFileName;
    }
}
