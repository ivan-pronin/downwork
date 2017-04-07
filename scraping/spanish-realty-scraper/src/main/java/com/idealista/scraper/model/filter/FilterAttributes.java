package com.idealista.scraper.model.filter;

public class FilterAttributes
{
    private PublicationDateFilter publicationDateFilter; // applicable on Idealista
    private boolean newHomes; // applicable on Fotocasa: Obra nueva (New homes)

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
}
