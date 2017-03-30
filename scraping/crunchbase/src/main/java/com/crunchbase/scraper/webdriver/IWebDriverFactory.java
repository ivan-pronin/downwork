package com.crunchbase.scraper.webdriver;

import com.crunchbase.scraper.webdriver.WebDriverFactory.DriverType;
import com.crunchbase.scraper.webdriver.proxy.ProxyAdapter;

import org.openqa.selenium.WebDriver;

public interface IWebDriverFactory
{
    WebDriver create(ProxyAdapter proxy, DriverType type);

    WebDriver create(DriverType type);
    
    void shutDown();
}
