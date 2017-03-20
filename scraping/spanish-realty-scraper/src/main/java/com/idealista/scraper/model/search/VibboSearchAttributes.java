package com.idealista.scraper.model.search;

import com.idealista.scraper.service.ScrapTarget;

public enum VibboSearchAttributes implements IGenericSearchAttributes
{
    TYPOLOGY,      // is mapped to drop-down list id="category_container": Selleccionar, Alquiler para vacaciones etc.
    OPERATION,     // is mapped to "TRANSACCIÓN" 
    PROPERTY_TYPE, // is mapped to "TIPO DE INMUEBLE"
    ADVERTISER;    // is mapped to "ANUNCIANTE" 

    @Override
    public String getIdentificationFlag()
    {
        return '_' + name() + '_';
    }

    @Override
    public ScrapTarget getScrapTarget()
    {
        return ScrapTarget.VIBBO;
    }
}
