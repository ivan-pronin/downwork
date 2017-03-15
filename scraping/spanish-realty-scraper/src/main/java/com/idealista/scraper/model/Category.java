package com.idealista.scraper.model;

import java.net.URL;
import java.util.Objects;

public class Category
{
    private URL url;
    private String state;
    private String type;
    private String subType;
    private String province;

    /**
     * Default constructor is used when we don't know, what Categories are used: this may
     * happen, when URLS to be processed are taken from file
     */
    public Category()
    {
        // nothing to do
    }

    public Category(URL url, Category templateCategory)
    {
        this.url = url;
        this.state = templateCategory.getState();
        this.type = templateCategory.getType();
        this.subType = templateCategory.getSubType();
        this.province = templateCategory.getProvince();
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

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }
}
