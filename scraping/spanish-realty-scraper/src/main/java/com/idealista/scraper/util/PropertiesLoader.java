package com.idealista.scraper.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public class PropertiesLoader
{
    private static Properties props;

    public static String getActiveProfile()
    {
        if (props == null)
        {
            props = initProperties();
        }
        return props.getProperty("scrapTarget");
    }

    private static Properties initProperties()
    {
        String filename = "./settings/realty.properties";
        try (Reader in = new FileReader(filename))
        {
            props = new Properties();
            props.load(in);
        }
        catch (IOException e)
        {
            System.out.println("Error while reading properties file: " + e.getMessage());
            return null;
        }
        return props;
    }
}
