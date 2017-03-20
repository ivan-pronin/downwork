package com.idealista.scraper.data;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.data.xls.XlsExporter;
import com.idealista.scraper.model.Advertisement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class DataSourceTests
{
    @Autowired
    private XlsExporter xlsExporter;
    
    @Test
    public void testName2() throws Exception
    {
        xlsExporter.initWorkBook();
        Advertisement ad = new Advertisement(new URL("http://www.tut.by"), "4_title_4");
        ad.setTags(Collections.emptyList());
        Set<Advertisement> ads = createSetOfAds(ad, 10);
        int iterations = 10;
        for (int i = 0; i < iterations; i++)
        {
            xlsExporter.writeResultsToXls(ads);
        }
    }
    
    private Set<Advertisement> createSetOfAds(Advertisement ad, int items) throws MalformedURLException
    {
        Set<Advertisement> results = new HashSet<>();
        URL u = ad.getUrl();
        for (int i = 0; i < items; i++)
        {
            Advertisement a = new Advertisement(new URL(u.toString() + "_" + i), ad.getTitle());
            a.setTags(Collections.emptyList());
            results.add(a);
        }
        return results;
    }

    // @Test
    public void testUpdateFile() throws Exception
    {
        try (FileWriter writer = new FileWriter("write.txt", true))
        {
            writer.write("a 1\n");
            writer.write("bb 22 \n");
        }
        catch (IOException e)
        {

        }
    }

    //@Test
    public void testName() throws Exception
    {
        String fileName = "./data/file.txt";
        File f = new File(fileName);
        if (!f.exists())
        {
            File parentFile = f.getParentFile();
            if (parentFile != null)
            {
                parentFile.mkdirs();
            }
            f.createNewFile();
            System.out.println("File created");
        }
    }
}
