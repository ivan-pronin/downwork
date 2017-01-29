package com.upwork.ivan.pronin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertiesLoader
{
    public static final String CHROME_DRIVER_PATH = "chromeDriverPath";
    public static Properties props;
    
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
        String filename = "app.properties";
        InputStream input = PropertiesLoader.class.getClassLoader().getResourceAsStream(filename);
        if (input == null) {
            System.out.println("Sorry, unable to find " + filename);
            return null;
        }
        try
        {
            props = new Properties();
            props.load(input);
        }
        catch (IOException e)
        {
            System.out.println("Error while reading properties file: " + e.getMessage());
            return null;
        }
        return props;
    }
}
