package com.idealista.scraper.data;

public enum DataType
{
    PROCESSED_ADS, NEW_ADS;

    public static DataType fromString(String text)
    {
        for (DataType item : DataType.values())
        {
            if (text.toLowerCase().contains(item.name().toLowerCase().replaceAll("_", "")))
            {
                return item;
            }
        }
        return null;
    }
}
