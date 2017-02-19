package com.idealista.scraper.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class PropertiesLoader
{
    private static final Logger LOGGER = LogManager.getLogger(PropertiesLoader.class);
    
    private static Properties props;

    public static Properties getProperties()
    {
        if (props == null)
        {
            return initProperties();
        }
        return props;
    }

    private static Properties initProperties()
    {
        String filename = "realty.properties";
        try (Reader in = new FileReader(filename))
        {
            props = new Properties();
            props.load(in);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while reading properties file: {}", e);
            return null;
        }
        return props;
    }
}
