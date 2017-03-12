package com.idealista.scraper.ui.page;

import com.idealista.scraper.ui.ClickActions;
import com.idealista.scraper.ui.SearchActions;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class BasePage
{
    @Autowired
    protected SearchActions searchActions;
    
    @Autowired
    protected ClickActions clickActions;
    
    public void setWebDriver(WebDriver driver)
    {
        searchActions = new SearchActions();
        clickActions = new ClickActions();
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
    }
}
