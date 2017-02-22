package com.idealista.scraper.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class MapPage
{
    private static final Logger LOGGER = LogManager.getLogger(MapPage.class);

    private WebDriver driver;

    public MapPage(WebDriver driver)
    {
        this.driver = driver;
    }

    public void clickShowAll()
    {
        List<WebElement> showAllLink = driver.findElements(By.id("showAllLink"));
        if (!showAllLink.isEmpty())
        {
            showAllLink.get(0).click();
        }
    }

    public void clickIdealistaLink()
    {
        List<WebElement> idealistaLink = driver
                .findElements(By.xpath("//div[@class='breadcrumb-geo wrapper clearfix']//a[text()='idealista']"));
        if (idealistaLink.isEmpty())
        {
            LOGGER.warn("Failed to find <idealista> link");
            return;
        }
        idealistaLink.get(0).click();
    }
}
