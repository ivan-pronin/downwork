package com.idealista.scraper.page;

import static org.junit.Assert.*;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class AdvertismentPageTests
{
    //@Test
    public void testGetPostalCode() throws Exception
    {
        Pattern pattern = Pattern.compile("(\\d{5,})");
        String text = " Avenida de Francia 3 San Bartolomé de Tirajana 3510034 ";
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            System.out.println(m.group(1));
        }
    }

    //@Test
    public void testGetSpecificCharacteristics() throws Exception
    {
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://www.idealista.com/en/inmueble/34020437/");
        List<WebElement> elements = driver
                .findElements(By.xpath("//h2[contains(.,'Specific characteristics')]/..//ul"));
        System.out.println(elements.size());
    }
    
    //@Test
    public void testGetPrice() throws Exception
    {
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://www.idealista.com/en/inmueble/26660304/");
        AdvertismentPage p = new AdvertismentPage(driver);
        System.out.println(p.getPrice());
    }
    
    //@Test
    public void getGetSize()
    {
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://www.idealista.com/en/inmueble/30322581/");
        AdvertismentPage p = new AdvertismentPage(driver);
        System.out.println(p.getSize());
        driver.navigate().to("https://www.idealista.com/en/inmueble/36004377/");
        p = new AdvertismentPage(driver);
        System.out.println(p.getSize());
    }
    
    @Test
    public void testGetTags() throws Exception
    {
        WebDriver driver = new ChromeDriver();
        driver.navigate().to("https://www.idealista.com/en/inmueble/30322581/");
        AdvertismentPage p = new AdvertismentPage(driver);
        System.out.println(p.getTags());
        driver.navigate().to("https://www.idealista.com/en/inmueble/36004377/");
        p = new AdvertismentPage(driver);
        System.out.println(p.getTags());
    }
}
