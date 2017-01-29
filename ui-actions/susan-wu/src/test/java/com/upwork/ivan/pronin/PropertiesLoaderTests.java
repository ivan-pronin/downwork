package com.upwork.ivan.pronin;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class PropertiesLoaderTests
{
    @Test
    public void testGetProperties()
    {
        Properties props = PropertiesLoader.getProperties();
        Assert.assertTrue(props.getProperty("chromeDriverPath").contains("chromedriver.exe"));
    }
}
