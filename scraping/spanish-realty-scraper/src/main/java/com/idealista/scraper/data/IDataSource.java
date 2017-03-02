package com.idealista.scraper.data;

import java.net.URL;
import java.util.Set;

public interface IDataSource
{
    Set<URL> getProcessedUrls(String fileName);
    
    void updateProcessedUrls(String fileName, Set<URL> urlsToAdd);
}
