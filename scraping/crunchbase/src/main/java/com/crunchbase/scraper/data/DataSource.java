package com.crunchbase.scraper.data;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Set;

@Component
public class DataSource implements IDataSource
{
    private static final Logger LOGGER = LogManager.getLogger(DataSource.class);

    private static Set<URL> cachedProcessedURLs;
    private static Set<URL> cachedNewURLs;

     @Override
    public void writeProxiesToFile(Set<String> proxies)
    {
        LOGGER.info("Writing <{}> proxies to file: ./proxies.txt", proxies.size());
        String fileName = "./proxies.txt";
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
}
