package com.idealista.scraper.model.search;

public class CategoryChoosingAttribute
{
    private String typology;
    private String operation;
    private String propertyType;
    private String advertiser;

    public String getAdvertiser()
    {
        return advertiser;
    }

    public String getOperation()
    {
        return operation;
    }

    public String getPropertyType()
    {
        return propertyType;
    }

    public String getTypology()
    {
        return typology;
    }

    public void setAdvertiser(String advertiser)
    {
        this.advertiser = advertiser;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public void setPropertyType(String propertyType)
    {
        this.propertyType = propertyType;
    }

    public void setTypology(String typology)
    {
        this.typology = typology;
    }

    @Override
    public String toString()
    {
        return "CategoryChoosingAttribute [typology=" + typology + ", operation=" + operation + ", propertyType="
                + propertyType + ", advertiser=" + advertiser + "]";
    }
}
