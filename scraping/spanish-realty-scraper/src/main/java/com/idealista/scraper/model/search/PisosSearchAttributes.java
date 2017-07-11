package com.idealista.scraper.model.search;

import com.idealista.scraper.service.ScrapTarget;

public enum PisosSearchAttributes implements IGenericSearchAttributes
{
    OPERATION, // is mapped to the first listbox on start page (Comprar / Alquiltar/ Obra nueva)
    TYPOLOGY,  // is mapped to the second : Casas y pisos, Locales y oficinas etc.
    LOCATION;  // is mapped to locations

    @Override
    public ScrapTarget getScrapTarget()
    {
        return ScrapTarget.PISOS;
    }

    @Override
    public String getIdentificationFlag()
    {
        return null;
    }
}
