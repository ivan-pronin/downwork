package com.idealista.scraper;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class AdvertisementExtractorTests
{
    @Test
    public void testName() throws Exception
    {
        
    }
    
    @Test
    public void testReadFiles() throws Exception
    {
        String fileName = "./data/idealista/processedAds.txt";
//        Set<String> lines = FileUtils.readFileToLines("./data/idealista/processedAds.txt");
//        System.out.println(lines.size());
        
        List<String> another = org.apache.commons.io.FileUtils.readLines(new File(fileName), "UTF-8");
        System.out.println(another.size());
        Set<String> uniqueList = new HashSet<>(another);
        System.out.println(uniqueList.size());
    }
}
