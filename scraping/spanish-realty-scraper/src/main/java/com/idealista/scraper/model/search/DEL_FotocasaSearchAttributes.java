package com.idealista.scraper.model.search;

import com.idealista.scraper.service.ScrapTarget;

public enum DEL_FotocasaSearchAttributes implements IGenericSearchAttributes
{
    OPERATION,      // is mapped to the four buttons on start page (Venta / Alquiler / Compartir / Vacacional)
    SEARCH_STRING;  // is mapped to search field

    @Override
    public ScrapTarget getScrapTarget()
    {
        return ScrapTarget.FOTOCASA;
    }

    @Override
    public String getIdentificationFlag()
    {
        return null;
    }
}
