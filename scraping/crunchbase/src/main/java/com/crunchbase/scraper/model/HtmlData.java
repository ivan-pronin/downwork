package com.crunchbase.scraper.model;

import java.net.URL;

public class HtmlData
{
    private URL url;
    private String fileName;

    public URL getUrl()
    {
        return url;
    }

    public void setUrl(URL url)
    {
        this.url = url;
    }

    public String getFileName()
    {
        return fileName;
    }

    public void setFileName(String fileName)
    {
        this.fileName = fileName;
    }

    @Override
    public String toString()
    {
        return "HtmlData [url=" + url + ", fileName=" + fileName + "]";
    }
}
