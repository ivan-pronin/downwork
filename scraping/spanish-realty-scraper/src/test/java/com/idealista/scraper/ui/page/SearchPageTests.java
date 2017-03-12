package com.idealista.scraper.ui.page;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class SearchPageTests
{
    @Test
    public void testClickProvince() throws Exception
    {
        WebDriver driver = new ChromeDriver();
        driver.get("https://www.idealista.com/venta-viviendas/madrid-provincia/");
        SearchPage page = new SearchPage();
        page.setWebDriver(driver);
        page.selectProvince("Zona sur");
        Assert.assertEquals("https://www.idealista.com/venta-viviendas/madrid/zona-sur/", driver.getCurrentUrl());
    }
}
