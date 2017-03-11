package com.idealista.scraper.data;

import com.idealista.scraper.util.FileUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class DataSource implements IDataSource
{
    private static final Logger LOGGER = LogManager.getLogger(DataSource.class);
    private static Set<URL> cachedProcessedURLs;
    private static Set<URL> cachedNewURLs;

    @Override
    public Set<URL> getUrlsFromFile(DataType type)
    {
        String fileName = type.getFileName();
        FileUtils.createFileIfNotExists(fileName);
        LOGGER.info("Getting {} URLs from file: {}", type.name(), fileName);
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
        initCachedUrls(type, result);
        return result;
    }

    @Override
    public void writeUrlsToFile(DataType type, Set<URL> urlsToAdd)
    {
        String fileName = type.getFileName();
        LOGGER.info("Writing <{}> URLs to {} file: {}", urlsToAdd.size(), type.name(), fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true)))
        {
            updateFile(type, urlsToAdd, writer);
            LOGGER.info("New <{}> URLs have been written to file: {}", urlsToAdd.size(), fileName);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing to file: {}", fileName);
        }
    }

    @Override
    public void removeUrlsFromFile(DataType type, Set<URL> urlsToRemove)
    {
        if (urlsToRemove.isEmpty())
        {
            LOGGER.warn("Nothing to remove..");
            return;
        }
        LOGGER.info("Removing <{}> URLs from {} file", urlsToRemove.size(), type.getFileName());
        Set<URL> cachedData = getCachedData(type);
        cachedData.removeAll(urlsToRemove);
        String fileName = type.getFileName();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
        {
            for (URL url : cachedData)
            {
                writer.write(url.toString() + System.lineSeparator());
            }
            LOGGER.info("Removed successfully");
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing to processedURLs file: {}", fileName);
        }
    }

    private void updateFile(DataType type, Set<URL> urlsToAdd, BufferedWriter writer) throws IOException
    {
        if (urlsToAdd.isEmpty())
        {
            LOGGER.warn("Nothing to update..");
            return;
        }
        Set<URL> cachedData = getCachedData(type);
        if (!cachedData.isEmpty())
        {
            urlsToAdd.removeAll(cachedData);
        }
        cachedData.addAll(urlsToAdd);
        for (URL url : urlsToAdd)
        {
            writer.write(url.toString() + System.lineSeparator());
        }
    }

    private void initCachedUrls(DataType type, Set<URL> data)
    {
        if (data.isEmpty())
        {
            return;
        }
        getCachedData(type).addAll(data);
    }

    private Set<URL> getCachedData(DataType type)
    {
        switch (type)
        {
            case PROCESSED_ADS:
                if (cachedProcessedURLs == null)
                {
                    cachedProcessedURLs = new HashSet<>();
                }
                return cachedProcessedURLs;
            case NEW_ADS:
                if (cachedNewURLs == null)
                {
                    cachedNewURLs = new HashSet<>();
                }
                return cachedNewURLs;
            default:
                return Collections.emptySet();
        }
    }
}
