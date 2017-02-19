package com.idealista.scraper.model;

public enum RealtyType
{
    NEW_CONSTRUCTION("New construction"),
    HOME("Homes"),
    HOLIDAY("Holiday"),
    ROOM("Rooms"),
    OFFICE("Offices"),
    COMMERCIAL_PROPERTY("Commercial property"),
    GARAGE("Garages"),
    STORAGE_ROOM("Storage rooms"),
    BUILDING("Buildings"),
    LAND("Land");
    
    private String alias;
    
    private RealtyType(String alias)
    {
        this.alias = alias;
    }
    public static RealtyType fromString(String text)
    {
        for (RealtyType type : RealtyType.values())
        {
            if (text.equalsIgnoreCase(type.alias))
            {
                return type;
            }
        }
        return null;
    }
}
