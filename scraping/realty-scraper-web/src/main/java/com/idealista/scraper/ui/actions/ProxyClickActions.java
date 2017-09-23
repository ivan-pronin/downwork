package com.idealista.scraper.ui.actions;

import org.openqa.selenium.WebDriver;
import org.springframework.stereotype.Component;

import com.idealista.scraper.ui.actions.base.BaseClickActions;

@Component
public class ProxyClickActions extends BaseClickActions
{
    private WebDriver webDriver;

    @Override
    protected WebDriver getWebDriver()
    {
        return webDriver;
    }

    public void setWebDriver(WebDriver webDriver)
    {
        this.webDriver = webDriver;
    }
}
