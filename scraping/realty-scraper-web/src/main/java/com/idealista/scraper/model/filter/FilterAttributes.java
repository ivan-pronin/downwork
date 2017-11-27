package com.idealista.scraper.model.filter;

import java.util.Set;

public class FilterAttributes
{
    private PublicationDateFilter publicationDateFilter; // applicable on Idealista
    private boolean newHomes; // applicable on Fotocasa: Obra nueva (New homes)
    private Set<String> disctricts; // applicable on Fotocasa on search page results

    /**
     * Applicable for Idealista
     */
    public enum PublicationDateFilter
    {
        NO_FILTER("#"), LAST_24_HOURS("con-publicado_ultimas-24-horas"), LAST_48_HOURS(
                "con-publicado_ultimas-48-horas"), LAST_WEEK(
                        "con-publicado_ultima-semana"), LAST_MONTH("con-publicado_ultimo-mes");

        private String relativeUrl;

        private PublicationDateFilter(String relativeUrl)
        {
            this.relativeUrl = relativeUrl;
        }

        public String getRelativeUrl()
        {
            return relativeUrl;
        }

        public static PublicationDateFilter fromString(String text)
        {
            if (text.contains("48"))
            {
                return PublicationDateFilter.LAST_48_HOURS;
            }

            if (text.contains("week"))
            {
                return PublicationDateFilter.LAST_WEEK;
            }
            if (text.contains("month"))
            {
                return PublicationDateFilter.LAST_MONTH;
            }
            return PublicationDateFilter.NO_FILTER;
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
