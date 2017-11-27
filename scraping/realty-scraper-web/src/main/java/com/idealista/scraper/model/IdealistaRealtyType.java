package com.idealista.scraper.model;

import org.apache.commons.lang3.StringUtils;

public enum IdealistaRealtyType
{
    NEW_CONSTRUCTION("New construction", "Obra nueva"), HOME("Homes", "Viviendas"), HOLIDAY("Holiday",
            "Vacacional"), ROOM("Rooms", "Habitación"), OFFICE("Offices", "Oficinas"), COMMERCIAL_PROPERTY(
                    "Commercial property", "Locales o naves"), GARAGE("Garages", "Garajes"), STORAGE_ROOM(
                            "Storage rooms", "Trasteros"), BUILDING("Buildings", "Edificios"), LAND("Land", "Terrenos");

    private String aliasEn;
    private String aliasEs;

    private IdealistaRealtyType(String aliasEn, String aliasEs)
    {
        this.aliasEn = aliasEn;
        this.aliasEs = aliasEs;
    }

    public static IdealistaRealtyType fromString(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            return null;
        }
        for (IdealistaRealtyType type : IdealistaRealtyType.values())
        {
            if (text.equalsIgnoreCase(type.aliasEn) || text.equalsIgnoreCase(type.aliasEs))
            {
                return type;
            }
        }
        return null;
    }

    public String getEnAlias()
    {
        return aliasEn;
    }

    public String getEsAlias()
    {
        return aliasEs;
    }
}
