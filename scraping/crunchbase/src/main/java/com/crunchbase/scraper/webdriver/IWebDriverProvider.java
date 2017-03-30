package com.crunchbase.scraper.webdriver;

import org.openqa.selenium.WebDriver;

public interface IWebDriverProvider
{
    WebDriver get();

    void end();

    boolean isWebDriverInitialized();

    void destroy();
}
