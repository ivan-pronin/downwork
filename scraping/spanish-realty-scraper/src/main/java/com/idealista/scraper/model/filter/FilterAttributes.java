package com.idealista.scraper.model.filter;

import java.util.Set;

public class FilterAttributes
{
    private PublicationDateFilter publicationDateFilter; // applicable on Idealista
    private boolean newHomes; // applicable on Fotocasa: Obra nueva (New homes)
    private Set<String> disctricts; //applicable on Fotocasa on search page results

    /**
     * Applicable for Idealista
     */
    public enum PublicationDateFilter
    {                                  
        NO_FILTER("default"),
        LAST_24_HOURS("con-publicado_ultimas-24-horas"),
        LAST_48_HOURS("1"),
        LAST_WEEK("2"),
        LAST_MONTH("3");

        private String relativeUrl;

        private PublicationDateFilter(String relativeUrl)
        {
            this.relativeUrl = relativeUrl;
        }

        public String getRelativeUrl()
        {
            return relativeUrl;
        }
    }

    public PublicationDateFilter getPublicationDateFilter()
    {
        return publicationDateFilter;
    }

    public void setPublicationDateFilter(PublicationDateFilter publicationDate)
    {
        this.publicationDateFilter = publicationDate;
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
        return "FilterAttributes [publicationDateFilter=" + publicationDateFilter + ", newHomes=" + newHomes + "]";
    }

    public Set<String> getDisctricts()
    {
        return disctricts;
    }

    public void setDisctricts(Set<String> disctricts)
    {
        this.disctricts = disctricts;
    }
}
