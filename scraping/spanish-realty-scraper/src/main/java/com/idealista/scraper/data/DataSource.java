package com.idealista.scraper.data;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

import com.idealista.scraper.util.FileUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class DataSource implements IDataSource
{
    private static final Logger LOGGER = LogManager.getLogger(DataSource.class);
    private static Set<URL> cachedProcessedURLs;

    @Override
    public Set<URL> getProcessedUrls(String fileName)
    {
        FileUtils.createFileIfNotExists(fileName);
        LOGGER.info("Getting processed URLs from file: {}", fileName);
        Set<String> data = FileUtils.readFileToLines(fileName);
        Set<URL> result = new HashSet<>();
        for (String row : data)
        {
            try
            {
                result.add(new URL(row));
            }
            catch (MalformedURLException e)
            {
                LOGGER.warn("Could not parse line: {} from file <{}>, {}", row, fileName, e.getMessage());
            }
        }
        LOGGER.info("Got <{}> URLs from file: {}", result.size(), fileName);
        initCachedUrls(result);
        return result;
    }

    private void initCachedUrls(Set<URL> result)
    {
        if (cachedProcessedURLs == null)
        {
            cachedProcessedURLs = new HashSet<>();
            cachedProcessedURLs.addAll(result);
        }
    }

    @Override
    public void updateProcessedUrls(String fileName, Set<URL> urlsToAdd)
    {
        LOGGER.info("Writing new <{}> URLs to processed URLs file: {}", urlsToAdd.size(), fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true)))
        {
            urlsToAdd.removeAll(cachedProcessedURLs);
            cachedProcessedURLs.addAll(urlsToAdd);
            for (URL url : urlsToAdd)
            {
                writer.write(url.toString() + System.lineSeparator());
            }
            LOGGER.info("New <{}> URLs have been written to file: {}", urlsToAdd.size(), fileName);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing to processedURLs file: {}", fileName);
        }
    }
}
