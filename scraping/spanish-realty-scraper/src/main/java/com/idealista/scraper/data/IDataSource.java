package com.idealista.scraper.data;

import java.net.URL;
import java.util.Set;

public interface IDataSource
{
    Set<URL> getUrlsFromFile(DataType type);

    void writeUrlsToFile(DataType type, Set<URL> urlsToAdd);

    void removeUrlsFromFile(DataType type, Set<URL> urlsToRemove);
}
