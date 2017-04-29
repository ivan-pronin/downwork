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
    private String district;
    private String subDistrict;

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
        if (templateCategory != null)
        {
            this.state = templateCategory.getState();
            this.type = templateCategory.getType();
            this.subType = templateCategory.getSubType();
            this.province = templateCategory.getProvince();
            this.district = templateCategory.getDistrict();
            this.subDistrict = templateCategory.getSubDistrict();
        }
    }

    public Category(URL url, String state, String type, String subType)
    {
        this.url = url;
        this.state = state;
        this.type = type;
        this.subType = subType;
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

    public String getProvince()
    {
        return province;
    }

    public String getState()
    {
        return state;
    }

    public String getSubType()
    {
        return subType;
    }

    public String getType()
    {
        return type;
    }

    public URL getUrl()
    {
        return url;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(url);
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public void setUrl(URL url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "Category [url=" + url + ", state=" + state + ", type=" + type + ", subType=" + subType + ", province="
                + province + ", district=" + district + ", subDistrict=" + subDistrict + "]";
    }

    public void setDistrict(String district)
    {
        this.district = district;
    }

    public String getSubDistrict()
    {
        return subDistrict;
    }

    public void setSubDistrict(String subDistrict)
    {
        this.subDistrict = subDistrict;
    }

    public String getDistrict()
    {
        return district;
    }
}
