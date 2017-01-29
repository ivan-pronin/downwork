package com.upwork.ivan.pronin;

import org.junit.Assert;
import org.openqa.selenium.WebDriver;

public class Application
{
    private static final String MAIN_PAGE_URL = "http://www.tut.by";

    public static void main(String[] args)
    {
           WebDriver driver = WebDriverProvider.getDriverInstance();
           driver.navigate().to(MAIN_PAGE_URL);
           System.out.println(driver.getCurrentUrl());
           Assert.assertTrue(driver.getCurrentUrl().contains("www.tut.by"));
           WebDriverProvider.stopDriver();
    }
}
