package com.idealista.web.config;

import org.springframework.stereotype.Component;

import com.idealista.scraper.model.IdealistaRealtyType;
import com.idealista.scraper.model.filter.FilterAttributes.PublicationDateFilter;

@Component
public class BaseSourceConfiguration
{

    private String operation = "Buy";
    private IdealistaRealtyType typology = IdealistaRealtyType.HOME;
    private String location = "Madrid";
    private String province;
    private PublicationDateFilter publicationDateFilter = PublicationDateFilter.NO_FILTER;
    private boolean privateAdsFiltering;
    private String language = "en";

    public String getOperation()
    {
        return operation;
    }

    public void setOperation(String operation)
    {
        this.operation = operation;
    }

    public IdealistaRealtyType getTypology()
    {
        return typology;
    }

    public void setTypology(String typology)
    {
        this.typology = IdealistaRealtyType.fromString(typology);
    }

    public String getLocation()
    {
        return location;
    }

    public void setLocation(String location)
    {
        this.location = location;
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public PublicationDateFilter getPublicationDateFilter()
    {
        return publicationDateFilter;
    }

    public void setPublicationDateFilter(String publicationDateFilter)
    {
        this.publicationDateFilter = PublicationDateFilter.fromString(publicationDateFilter);
    }

    public boolean isPrivateAdsFiltering()
    {
        return privateAdsFiltering;
    }

    public void setPrivateAdsFiltering(boolean privateAdsFiltering)
    {
        this.privateAdsFiltering = privateAdsFiltering;
    }

    public String getLanguage()
    {
        return language;
    }

    public void setLanguage(String language)
    {
        this.language = language;
    }

    @Override
    public String toString()
    {
        return "BaseSourceConfiguration [operation=" + operation + ", typology=" + typology + ", location=" + location
                + ", province=" + province + ", publicationDateFilter=" + publicationDateFilter
                + ", privateAdsFiltering=" + privateAdsFiltering + ", language=" + language + "]";
    }

}
