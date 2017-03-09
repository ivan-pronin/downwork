package com.idealista.scraper.data;

public enum DataType
{
    PROCESSED_ADS("./data/processedAds.txt"), NEW_ADS("./data/newAds.txt");

    private String fileName;

    private DataType(String fileName)
    {
        this.fileName = fileName;
    }

    public String getFileName()
    {
        return fileName;
    }
}
