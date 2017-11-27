package com.idealista.scraper.webdriver;

import java.net.URL;

import org.openqa.selenium.WebDriver;

public interface INavigateActions
{
    void navigateWithoutValidations(String page);

    WebDriver get(URL page);

    WebDriver get(String page);
}
