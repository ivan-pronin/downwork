package com.idealista.scraper.webdriver;

import org.openqa.selenium.WebDriver;

import com.idealista.scraper.webdriver.WebDriverFactory.DriverType;
import com.idealista.scraper.webdriver.proxy.ProxyAdapter;

public interface IWebDriverFactory
{
    WebDriver create(ProxyAdapter proxy, DriverType type);

    WebDriver create(DriverType type);

    void shutDown();
}
