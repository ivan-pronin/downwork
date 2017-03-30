package com.crunchbase.scraper.model;

import java.util.HashSet;
import java.util.Set;

public class Company
{
    private String title;
    private Set<HtmlData> htmlData = new HashSet<>();
    
    public Company(String title)
    {
        this.title = title;
    }

    public String getTitle()
    {
        return title;
    }

    public void setTitle(String title)
    {
        this.title = title;
    }

    public Set<HtmlData> getHtmlData()
    {
        return htmlData;
    }

    public void setHtmlData(Set<HtmlData> htmlData)
    {
        this.htmlData = htmlData;
    }

    @Override
    public String toString()
    {
        return "Company [title=" + title + "]";
    }
}
