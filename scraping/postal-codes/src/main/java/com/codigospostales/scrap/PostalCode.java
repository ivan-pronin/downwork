package com.codigospostales.scrap;

import java.util.Objects;
import java.util.Set;

public class PostalCode
{
    private String number;
    private String province; 
    private Set<String> streets;

    @Override
    public int hashCode()
    {
        return Objects.hashCode(number);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        PostalCode other = (PostalCode) obj;
        return Objects.equals(number, other.number);
    }

    public String getNumber()
    {
        return number;
    }

    public Set<String> getStreets()
    {
        return streets;
    }

    public void setNumber(String number)
    {
        this.number = number;
    }

    public void setStreets(Set<String> streets)
    {
        this.streets = streets;
    }

    @Override
    public String toString()
    {
        return "PostalCode [number=" + number + ", streetsCount=" + streets.size() + "]";
    }

    public String getProvince()
    {
        return province;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }
}
