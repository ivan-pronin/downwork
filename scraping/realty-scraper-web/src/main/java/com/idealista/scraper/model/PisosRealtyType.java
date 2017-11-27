package com.idealista.scraper.model;

import org.apache.commons.lang3.StringUtils;

public enum PisosRealtyType
{
    HOUSES_AND_FLATS("Casas y pisos"),
    OFFICE("Locales y oficinas"),
    COMMERCIAL_PROPERTY("Naves"),
    LAND("Terrenos"),
    GARAGE("Garajes y trasteros");
    
    private String aliasEs;
    
    private PisosRealtyType(String aliasEs)
    {
        this.aliasEs = aliasEs;
    }

    public static PisosRealtyType fromString(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            return null;
        }
        for (PisosRealtyType type : PisosRealtyType.values())
        {
            if (text.equalsIgnoreCase(type.aliasEs))
            {
                return type;
            }
        }
        return null;
    }
}
