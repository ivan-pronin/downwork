package com.idealista.scraper.model.search;

import java.util.Set;

public class SearchAttributes
{
    private Set<String> operations;
    private Set<String> typologies;
    private Set<String> locations;
    private Set<String> propertyTypes;
    private String advertiser;

    public SearchAttributes ()
    {
        //nothing to do
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

    @Override
    public String toString()
    {
        return "SearchAttributes [operations=" + operations + ", typologies=" + typologies + ", locations=" + locations
                + "]";
    }
}
