package com.idealista.scraper.ui.page;

import com.idealista.scraper.ui.ClickActions;
import com.idealista.scraper.ui.SearchActions;

import org.openqa.selenium.WebDriver;

public class BasePage
{
    protected SearchActions searchActions = new SearchActions();
    protected ClickActions clickActions = new ClickActions();

    public void setWebDriver(WebDriver driver)
    {
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
    }
}
