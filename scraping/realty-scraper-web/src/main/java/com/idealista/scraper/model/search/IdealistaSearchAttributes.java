package com.idealista.scraper.model.search;

import com.idealista.scraper.service.ScrapTarget;

public enum IdealistaSearchAttributes implements IGenericSearchAttributes
{
    OPERATION, // is mapped to the first listbox on start page (Comprar / Alquiltar/ Compartir)
    TYPOLOGY, // is mapped to the second : Obra nueva, Viviendas etc.
    LOCATION; // is mapped to locations

    @Override
    public ScrapTarget getScrapTarget()
    {
        return ScrapTarget.IDEALISTA;
    }

    @Override
    public String getIdentificationFlag()
    {
        return null;
    }
}
