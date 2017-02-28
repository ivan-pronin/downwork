package com.idealista.scraper.driver;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashMap;

import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverFactoryTests
{
    @Test
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
    
    //@Test
    public void testCheckOptions() throws Exception
    {
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setJavascriptEnabled(false);
        ChromeOptions options = new ChromeOptions();
        //options.addArguments("incognito", );
        options.addArguments(Arrays.asList("test-type", "no-sandbox"));
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        WebDriver driver = new ChromeDriver(cap);
        driver.navigate().to("https://www.idealista.com/venta-viviendas/alicante/");
    }
}
