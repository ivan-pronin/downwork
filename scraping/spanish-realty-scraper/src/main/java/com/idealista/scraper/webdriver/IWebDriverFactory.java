package com.idealista.scraper.webdriver;

import com.idealista.scraper.proxy.ProxyAdapter;
import com.idealista.scraper.webdriver.WebDriverFactory.DriverType;

import org.openqa.selenium.WebDriver;

public interface IWebDriverFactory
{
    WebDriver create(ProxyAdapter proxy, DriverType type);

    WebDriver create(DriverType type);
}
