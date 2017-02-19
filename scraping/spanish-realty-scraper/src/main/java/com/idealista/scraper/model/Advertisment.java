package com.idealista.scraper.model;

import java.net.URL;
import java.util.Objects;

public class Advertisment
{
    // ok Title +++               
    // ok Type +++
    //    Subtype ???
    // ok Date of listing +++111
    //    Number of views - requires email registration, will add
    // ok Address +++
    // ok State = location? +++
    //   City - not clear how to parse
    // ok Postal Code +++
    //   Age - not clear where to take info?
    // ok  Description +++
    // ok  Bedrooms +++
    // ok Bathrooms +++
    // ok Size +++
    // ok Price +++
    //   Certificacion energetica - not clear
    //   Profesional or Particular +++
    // ok  Listing Agent +++
    //   Phone Listing Agent +++
    //   Email Listing Agent -
    // ok  Link +++
    // ok  Images (yes /no) +++
    // Tag1
    // Tag2
    // .... more tags

    private URL url;
    private String title;
    private RealtyType type;
    private String dateOfListing;
    private int numberOfViews;
    private String address;
    private String state;
    private String city;
    private String postalCode;
    private String description;
    private int bedRooms;
    private int bathRooms;
    private int size;
    private int price;
    private String professional;
    private String agent;
    private boolean hasImages;

    public Advertisment(URL url, String title, RealtyType type)
    {
        this.url = url;
        this.title = title;
        this.type = type;
    }

    @Override
    public String toString()
    {
        return "Advertisment [title=" + title + ", type=" + type + ", dateOfListing=" + dateOfListing + ", address="
                + address + "]";
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(url);
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
        Advertisment other = (Advertisment) obj;
        return Objects.equals(url, other.url);
    }

    public URL getUrl()
    {
        return url;
    }

    public void setUrl(URL url)
    {
        this.url = url;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public RealtyType getType()
    {
        return type;
    }

    public void setType(RealtyType type)
    {
        this.type = type;
    }

    public String getDateOfListing()
    {
        return dateOfListing;
    }

    public void setDateOfListing(String dateOfListing)
    {
        this.dateOfListing = dateOfListing;
    }

    public int getNumberOfViews()
    {
        return numberOfViews;
    }

    public void setNumberOfViews(int numberOfViews)
    {
        this.numberOfViews = numberOfViews;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getState()
    {
        return state;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public String getCity()
    {
        return city;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public String getDescription()
    {
        return description;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public int getBedRooms()
    {
        return bedRooms;
    }

    public void setBedRooms(int bedRooms)
    {
        this.bedRooms = bedRooms;
    }

    public int getBathRooms()
    {
        return bathRooms;
    }

    public void setBathRooms(int bathRooms)
    {
        this.bathRooms = bathRooms;
    }

    public int getSize()
    {
        return size;
    }

    public void setSize(int size)
    {
        this.size = size;
    }

    public int getPrice()
    {
        return price;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public String getProfessional()
    {
        return professional;
    }

    public void setProfessional(String professional)
    {
        this.professional = professional;
    }

    public String getAgent()
    {
        return agent;
    }

    public void setAgent(String agent)
    {
        this.agent = agent;
    }

    public boolean isHasImages()
    {
        return hasImages;
    }

    public void setHasImages(boolean hasImages)
    {
        this.hasImages = hasImages;
    }
}
