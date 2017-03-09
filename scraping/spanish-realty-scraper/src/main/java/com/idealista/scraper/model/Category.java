package com.idealista.scraper.model;

import java.net.URL;
import java.util.Objects;

public class Category
{
    private URL url;
    private String state;
    private String type;
    private String subType;

    public Category(URL url, Category templateCategory)
    {
        this.url = url;
        this.state = templateCategory.getState();
        this.type = templateCategory.getType();
        this.subType = templateCategory.getSubType();
    }

    public Category(URL url, String state, String type, String subType)
    {
        this.url = url;
        this.state = state;
        this.type = type;
        this.subType = subType;
    }

    public URL getUrl()
    {
        return url;
    }

    public String getState()
    {
        return state;
    }

    public String getType()
    {
        return type;
    }

    public String getSubType()
    {
        return subType;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(url);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Category other = (Category) obj;
        return Objects.equals(url, other.url);
    }

    @Override
    public String toString()
    {
        return "Category [url=" + url + "]";
    }
}
