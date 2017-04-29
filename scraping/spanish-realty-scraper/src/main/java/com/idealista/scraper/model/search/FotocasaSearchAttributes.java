package com.idealista.scraper.model.search;

import com.idealista.scraper.service.ScrapTarget;

public enum FotocasaSearchAttributes implements IGenericSearchAttributes
{
    OPERATION,  // is mapped to the four buttons on start page (Venta / Alquiler / Compartir / Vacacional)
    DISTRICT;   // is mapped to district dropdown on the search results page

    @Override
    public ScrapTarget getScrapTarget()
    {
        return ScrapTarget.FOTOCASA;
    }

    @Override
    public String getIdentificationFlag()
    {
        return '_' + name() + '_';
    }
}
