package com.idealista.scraper.driver;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.logging.Logs;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.HashMap;
import java.util.logging.Level;

public class WebDriverFactoryTests
{
    //@Test
    public void testDisableImages() throws Exception
    {
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setJavascriptEnabled(false);

        HashMap<String, Object> images = new HashMap<String, Object>(); 
        images.put("images", 2);
        HashMap<String, Object> prefs = new HashMap<String, Object>(); 
        prefs.put("profile.default_content_setting_values", images);

        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito","disable-extensions","disable-plugins");
        options.addArguments("test-type","no-sandbox");
        options.addArguments("enable-strict-powerful-feature-restrictions");

        options.setExperimentalOption("prefs", prefs); 
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver = new ChromeDriver(cap);
        driver.navigate().to("https://www.idealista.com/venta-viviendas/alicante/");
    }
    
    @Test
    public void testCheckOptions() throws Exception
    {
        System.setProperty("webdriver.chrome.logfile", "./chromedriver_" + getTimeStamp() + ".log");
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setJavascriptEnabled(false);
        ChromeOptions options = new ChromeOptions();
        options.addArguments("incognito");
        
        LoggingPreferences logs = new LoggingPreferences();
        logs.enable(LogType.BROWSER, Level.ALL);
        //logs.enable(LogType.CLIENT, Level.OFF);
        logs.enable(LogType.DRIVER, Level.ALL);
        //logs.enable(LogType.PERFORMANCE, Level.OFF);
        //logs.enable(LogType.PROFILER, Level.OFF);
        //logs.enable(LogType.SERVER, Level.OFF);
        
        options.addArguments(Arrays.asList("test-type", "no-sandbox", "verbose"));
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        cap.setCapability(CapabilityType.LOGGING_PREFS, logs);
        WebDriver driver = new ChromeDriver(cap);
        //driver.navigate().to("https://www.idealista.com/venta-viviendas/alicante/");
        driver.navigate().to("https://www.onliner.by");
        Logs logzz = driver.manage().logs();
        for (String type : logzz.getAvailableLogTypes())
        {
            System.out.println(logzz.get(type).getAll().size());
        }
        System.out.println("======================");
        WebDriver driver2 = new ChromeDriver();
        driver2.navigate().to("https://www.onliner.by");
        Logs logzz2 = driver2.manage().logs();
        for (String type : logzz2.getAvailableLogTypes())
        {
            System.out.println(logzz2.get(type).getAll().size());
        }
    }
    
    private static String getTimeStamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH-mm-ss_SSS");
        return sdf.format(new Timestamp(new java.util.Date().getTime()));
    }
}
