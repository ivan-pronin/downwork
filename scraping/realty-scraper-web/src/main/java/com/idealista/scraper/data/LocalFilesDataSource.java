package com.idealista.scraper.data;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import com.idealista.scraper.util.FileUtils;

@Component
public class LocalFilesDataSource implements ILocalFilesDataSource
{
    private static final Logger LOGGER = LogManager.getLogger(LocalFilesDataSource.class);

    private static Set<URL> cachedProcessedURLs;
    private static Set<URL> cachedNewURLs;

    @Override
    public Set<URL> getUrlsFromFile(String fileName)
    {
        FileUtils.createFileIfNotExists(fileName);
        LOGGER.info("Getting URLs from file: {}", fileName);
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
        initCachedUrls(DataType.fromString(fileName), result);
        return result;
    }

    @Override
    public void removeUrlsFromFile(String fileName, Set<URL> urlsToRemove)
    {
        if (urlsToRemove.isEmpty())
        {
            LOGGER.warn("Nothing to remove..");
            return;
        }
        LOGGER.info("Removing <{}> URLs from {} file", urlsToRemove.size(), fileName);
        Set<URL> cachedData = getCachedData(DataType.fromString(fileName));
        cachedData.removeAll(urlsToRemove);
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

    @Override
    public void writeProxiesToFile(Set<String> proxies)
    {
        LOGGER.info("Writing <{}> proxies to file: /settings/proxies.txt", proxies.size());
        String fileName = "./settings/proxies.txt";
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName)))
        {
            for (String proxy : proxies)
            {
                writer.write(proxy + System.lineSeparator());
            }
            LOGGER.info("Proxies file has been updated.");
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing to file: {}", fileName);
        }
    }

    @Override
    public void writeUrlsToFile(String fileName, Set<URL> urlsToAdd)
    {
        LOGGER.info("Writing <{}> URLs to file: {}", urlsToAdd.size(), fileName);
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName, true)))
        {
            updateFile(fileName, urlsToAdd, writer);
            LOGGER.info("New <{}> URLs have been written to file: {}", urlsToAdd.size(), fileName);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing to file: {}", fileName);
        }
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

    private void initCachedUrls(DataType type, Set<URL> data)
    {
        if (data.isEmpty())
        {
            return;
        }
        getCachedData(type).addAll(data);
    }

    private void updateFile(String fileName, Set<URL> urlsToAdd, BufferedWriter writer) throws IOException
    {
        if (urlsToAdd.isEmpty())
        {
            LOGGER.warn("Nothing to update..");
            return;
        }
        Set<URL> cachedData = getCachedData(DataType.fromString(fileName));
        if (!cachedData.isEmpty())
        {
            urlsToAdd.removeAll(cachedData);
        }
        cachedData.addAll(urlsToAdd);
        for (URL url : urlsToAdd)
        {
            if (url == null)
            {
                continue;
            }
            writer.write(url.toString() + System.lineSeparator());
        }
    }
}
