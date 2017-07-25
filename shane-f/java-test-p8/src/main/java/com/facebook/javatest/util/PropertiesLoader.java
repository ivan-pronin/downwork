package com.facebook.javatest.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

public final class PropertiesLoader
{
    public static Properties props;

    private PropertiesLoader()
    {
    }
    
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
        String filename = "facebooktest.properties";
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
