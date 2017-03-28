package com.idealista.scraper.data;

import java.net.URL;
import java.util.Set;

public interface IDataSource
{
    Set<URL> getUrlsFromFile(String fileName);

    void writeUrlsToFile(String fileName, Set<URL> urlsToAdd);

    void removeUrlsFromFile(String fileName, Set<URL> urlsToRemove);
    
    void writeProxiesToFile(Set<String> proxies);
}
