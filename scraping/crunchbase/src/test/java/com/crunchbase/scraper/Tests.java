package com.crunchbase.scraper;

import static org.junit.Assert.*;

import com.crunchbase.scraper.model.Company;
import com.crunchbase.scraper.model.HtmlData;
import com.crunchbase.scraper.util.DateTimeUtils;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.interactions.Actions;

import java.util.HashSet;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class Tests
{

    @Test
    public void testName3() throws Exception
    {
        FirefoxProfile profile = new FirefoxProfile();
        profile.setPreference("permissions.default.image", 2);
        profile.setPreference("permissions.default.stylesheet", 2);
        profile.setPreference("dom.ipc.plugins.enabled.libflashplayer.so", false);
        WebDriver driver = new FirefoxDriver(profile);
        driver.get("https://www.crunchbase.com/organization/perch#/entity");
    }
    
    
    public void testName() throws Exception
    {
        long heapsize = Runtime.getRuntime().totalMemory();
        System.out.println("heapsize is :: " + heapsize);
        System.out.println("maxmemory is :: " + Runtime.getRuntime().maxMemory());
    }

    private Company createCompany(String title)
    {
        Company c = new Company(title);
        Set<HtmlData> data = createData();
        c.setHtmlData(data);
        return c;
    }

    private Set<HtmlData> createData()
    {
        Set<HtmlData> result = new HashSet<>();
        for (int i = 0; i < new Random().nextInt(5); i++)
        {
            HtmlData item = new HtmlData();
            item.setFileName("fileName_" + i);
            result.add(item);
        }
        return result;
    }
}
