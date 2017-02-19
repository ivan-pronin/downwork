package com.idealista.scraper.search;

public class SearchAttribute
{
    private String operation;
    private String typology;
    private String location;

    public SearchAttribute(String operation, String typology, String location)
    {
        this.operation = operation;
        this.typology = typology;
        this.location = location;
    }

    public String getOperation()
    {
        return operation;
    }

    public String getTypology()
    {
        return typology;
    }

    public String getLocation()
    {
        return location;
    }

}
