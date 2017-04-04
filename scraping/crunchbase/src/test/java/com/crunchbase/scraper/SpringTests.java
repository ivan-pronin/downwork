package com.crunchbase.scraper;

import static org.junit.Assert.*;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = CrunchbaseAppConfig.class)
public class SpringTests
{
    @Value("${targetFile}")
    private String targetFile;
    
    @Test
    public void testName() throws Exception
    {
        System.out.println(targetFile.split("\\.")[0]);
    }
}
