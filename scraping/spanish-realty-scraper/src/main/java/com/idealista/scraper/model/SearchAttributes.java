package com.idealista.scraper.model;

import java.util.Set;

public class SearchAttributes
{
    private Set<String> operations;
    private Set<String> typologies;
    private Set<String> locations;

    public SearchAttributes(Set<String> operations, Set<String> typologies, Set<String> locations)
    {
        this.operations = operations;
        this.typologies = typologies;
        this.locations = locations;
    }

    public Set<String> getOperations()
    {
        return operations;
    }

    public Set<String> getTypologies()
    {
        return typologies;
    }

    public Set<String> getLocations()
    {
        return locations;
    }

    @Override
    public String toString()
    {
        return "SearchAttributes [operations=" + operations + ", typologies=" + typologies + ", locations=" + locations
                + "]";
    }
}
