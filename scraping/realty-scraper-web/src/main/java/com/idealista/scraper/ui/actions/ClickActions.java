package com.idealista.scraper.ui.actions;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.scraper.ui.actions.base.BaseClickActions;
import com.idealista.scraper.webdriver.WebDriverProvider;

@Component
public class ClickActions extends BaseClickActions
{
    @Autowired
    private WebDriverProvider webDriverProvider;

    @Override
    protected WebDriver getWebDriver()
    {
        return webDriverProvider.get();
    }

}
