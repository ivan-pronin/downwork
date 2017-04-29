package com.idealista.scraper.model.search;

public class CategoryChoosingAttribute
{
    private String typology;
    private String operation;
    private String propertyType;
    private String advertiser;
    private String district;
    private String searchString;
    private boolean newHomes;

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

    public String getDistrict()
    {
        return district;
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public String getSearchString()
    {
        return searchString;
    }

    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }

    public boolean isNewHomes()
    {
        return newHomes;
    }

    public void setNewHomes(boolean newHomes)
    {
        this.newHomes = newHomes;
    }

    @Override
    public String toString()
    {
        return "CategoryChoosingAttribute [typology=" + typology + ", operation=" + operation + ", propertyType="
                + propertyType + ", advertiser=" + advertiser + ", district=" + district + ", searchString="
                + searchString + ", newHomes=" + newHomes + "]";
    }
}
