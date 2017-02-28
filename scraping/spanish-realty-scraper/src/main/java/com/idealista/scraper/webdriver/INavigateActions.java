package com.idealista.scraper.webdriver;

import org.openqa.selenium.WebDriver;

import java.net.URL;

public interface INavigateActions
{
    void navigateWithoutValidations(String page);

    WebDriver get(URL page);
}
