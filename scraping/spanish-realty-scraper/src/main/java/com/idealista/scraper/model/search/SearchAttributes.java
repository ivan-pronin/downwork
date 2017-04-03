package com.idealista.scraper.model.search;

import java.util.Set;

public class SearchAttributes
{
    /**
     * Applicable for Idealista, Vibbo, Fotocasa
     * E.g. Buy, Rent, Share
     */
    private Set<String> operations;

    /**
     * Applicable for Idealista, Vibbo
     * E.g. Homes, Commercial property, Garages
     */
    private Set<String> typologies;

    /**
     * Applicable for Idealista - combobox options
     * E.g. Madrid
     */
    private Set<String> locations;

    /**
     * Applicable for Vibbo
     * E.g. casa adosada
     */
    private Set<String> propertyTypes;

    /**
     * Applicable for Vibbo
     * E.g. professional
     */
    private String advertiser;

    /**
     * Applicable for Fotocasa
     * Any string to search for. Entered into search field, e.g. Madrid
     */
    private String searchString;

    public SearchAttributes()
    {
        // nothing to do
    }

    public SearchAttributes(Set<String> operations, Set<String> typologies, Set<String> locations)
    {
        this.operations = operations;
        this.typologies = typologies;
        this.locations = locations;
    }

    public String getAdvertiser()
    {
        return advertiser;
    }

    public Set<String> getLocations()
    {
        return locations;
    }

    public Set<String> getOperations()
    {
        return operations;
    }

    public Set<String> getPropertyTypes()
    {
        return propertyTypes;
    }

    public Set<String> getTypologies()
    {
        return typologies;
    }

    public void setAdvertiser(String advertiser)
    {
        this.advertiser = advertiser;
    }

    public void setOperations(Set<String> operations)
    {
        this.operations = operations;
    }

    public void setPropertyTypes(Set<String> propertyTypes)
    {
        this.propertyTypes = propertyTypes;
    }

    public void setTypologies(Set<String> typologies)
    {
        this.typologies = typologies;
    }

    public String getSearchString()
    {
        return searchString;
    }

    public void setSearchString(String searchString)
    {
        this.searchString = searchString;
    }

    @Override
    public String toString()
    {
        return "SearchAttributes [operations=" + operations + ", typologies=" + typologies + ", locations=" + locations
                + ", propertyTypes=" + propertyTypes + ", advertiser=" + advertiser + ", searchString=" + searchString
                + "]";
    }
}
