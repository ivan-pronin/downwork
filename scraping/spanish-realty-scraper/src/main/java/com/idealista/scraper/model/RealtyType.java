package com.idealista.scraper.model;

import org.apache.commons.lang3.StringUtils;

public enum RealtyType
{
    NEW_CONSTRUCTION("New construction", "Obra nueva"),
    HOME("Homes", "Viviendas"),
    HOLIDAY("Holiday", "Vacacional"),
    ROOM("Rooms", "Habitación"),
    OFFICE("Offices", "Oficinas"),
    COMMERCIAL_PROPERTY("Commercial property", "Locales o naves"),
    GARAGE("Garages", "Garajes"),
    STORAGE_ROOM("Storage rooms", "Trasteros"),
    BUILDING("Buildings", "Edificios"),
    LAND("Land", "Terrenos");
    
    private String aliasEn;
    private String aliasEs;
    
    private RealtyType(String aliasEn, String aliasEs)
    {
        this.aliasEn = aliasEn;
        this.aliasEs = aliasEs;
    }

    public static RealtyType fromString(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            return null;
        }
        for (RealtyType type : RealtyType.values())
        {
            if (text.equalsIgnoreCase(type.aliasEn) || text.equalsIgnoreCase(type.aliasEs))
            {
                return type;
            }
        }
        return null;
    }
}
