package com.idealista.scraper.model;

import java.net.URL;
import java.util.List;
import java.util.Objects;

public class Advertisement
{
    // ok Title +++               
    // ok Type +++
    // ok Subtype - equals to <operation>
    // new Province
    // ok Date of listing +++111
    // ok Number of views - requires email registration, will add
    // ok Address +++
    // ok State = location? +++
    // ok City 
    // ok Postal Code +++
    // ok Age - not clear where to take info?
    // ok  Description +++
    // ok  Bedrooms +++
    // ok Bathrooms +++
    // ok Size +++
    // ok Price +++
    // ok Certificacion energetica - not clear
    // ok Profesional or Particular +++
    // ok Listing Agent +++
    // ok Phone Listing Agent +++
    // ok Email Listing Agent -
    // ok  Link +++
    // ok  Images (yes /no) +++
    // ok all Tags
    // ok more tags

    private URL url;
    private String title;
    private String type;
    private String subType;
    private String province;
    private String dateOfListing;
    private String numberOfViews;
    private String address;
    private String state;
    private String city;
    private String age;
    private String postalCode;
    private String description;
    private int bedRooms;
    private int bathRooms;
    private String size;
    private int price;
    private String energyCertification;
    private String professional;
    private String agent;
    private String agentPhone;
    private String agentEmail;
    private boolean hasImages;
    private List<String> tags;
    
    public Advertisement(URL url, String title)
    {
        this.url = url;
        this.title = title;
    }

    public Advertisement(URL url, String title, String type)
    {
        this.url = url;
        this.title = title;
        this.type = type;
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
        Advertisement other = (Advertisement) obj;
        return Objects.equals(url, other.url);
    }

    public String getAddress()
    {
        return address;
    }

    public String getAge()
    {
        return age;
    }

    public String getAgent()
    {
        return agent;
    }

    public String getAgentEmail()
    {
        return agentEmail;
    }

    public String getAgentPhone()
    {
        return agentPhone;
    }

    public int getBathRooms()
    {
        return bathRooms;
    }

    public int getBedRooms()
    {
        return bedRooms;
    }

    public String getCity()
    {
        return city;
    }

    public String getDateOfListing()
    {
        return dateOfListing;
    }

    public String getDescription()
    {
        return description;
    }

    public String getEnergyCertification()
    {
        return energyCertification;
    }

    public String getNumberOfViews()
    {
        return numberOfViews;
    }

    public String getPostalCode()
    {
        return postalCode;
    }

    public int getPrice()
    {
        return price;
    }

    public String getProfessional()
    {
        return professional;
    }

    public String getProvince()
    {
        return province;
    }

    public String getSize()
    {
        return size;
    }

    public String getState()
    {
        return state;
    }

    public String getSubType()
    {
        return subType;
    }

    public List<String> getTags()
    {
        return tags;
    }

    public String getTitle()
    {
        return title;
    }

    public String getType()
    {
        return type;
    }

    public URL getUrl()
    {
        return url;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(url);
    }

    public boolean hasImages()
    {
        return hasImages;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public void setAge(String age)
    {
        this.age = age;
    }

    public void setAgent(String agent)
    {
        this.agent = agent;
    }

    public void setAgentEmail(String agentEmail)
    {
        this.agentEmail = agentEmail;
    }

    public void setAgentPhone(String agentPhone)
    {
        this.agentPhone = agentPhone;
    }

    public void setBathRooms(int bathRooms)
    {
        this.bathRooms = bathRooms;
    }

    public void setBedRooms(int bedRooms)
    {
        this.bedRooms = bedRooms;
    }

    public void setCity(String city)
    {
        this.city = city;
    }

    public void setDateOfListing(String dateOfListing)
    {
        this.dateOfListing = dateOfListing;
    }

    public void setDescription(String description)
    {
        this.description = description;
    }

    public void setEnergyCertification(String energyCertification)
    {
        this.energyCertification = energyCertification;
    }

    public void setHasImages(boolean hasImages)
    {
        this.hasImages = hasImages;
    }

    public void setNumberOfViews(String numberOfViews)
    {
        this.numberOfViews = numberOfViews;
    }

    public void setPostalCode(String postalCode)
    {
        this.postalCode = postalCode;
    }

    public void setPrice(int price)
    {
        this.price = price;
    }

    public void setProfessional(String professional)
    {
        this.professional = professional;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public void setSize(String size)
    {
        this.size = size;
    }

    public void setState(String state)
    {
        this.state = state;
    }

    public void setSubType(String subType)
    {
        this.subType = subType;
    }

    public void setTags(List<String> tags)
    {
        this.tags = tags;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public void setUrl(URL url)
    {
        this.url = url;
    }

    @Override
    public String toString()
    {
        return "Advertisement [title=" + title + ", type=" + type + ", dateOfListing=" + dateOfListing + ", address="
                + address + "]";
    }
}
