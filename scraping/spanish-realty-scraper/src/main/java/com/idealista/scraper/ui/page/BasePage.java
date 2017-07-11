package com.idealista.scraper.ui.page;

import com.idealista.scraper.ui.actions.ClickActions;
import com.idealista.scraper.ui.actions.SearchActions;
import com.idealista.scraper.ui.actions.WaitActions;

import org.openqa.selenium.WebDriver;

public abstract class BasePage
{
    protected static final String TODO = "// TODO";
    protected static final String INFO_NOT_FOUND = "Info not found";
    protected static final String NA_FOR_THIS_SITE = "N/A for this site";
    protected static final String PRIVATE = "Private";
    protected static final String PROFESSIONAL = "Professional";

    protected SearchActions searchActions = new SearchActions();
    protected ClickActions clickActions = new ClickActions();
    protected WaitActions waitActions = new WaitActions();

    public void setWebDriver(WebDriver driver)
    {
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
        waitActions.setWebDriver(driver);
    }

    protected String isProfessional(String listingAgent)
    {
        if (listingAgent != null)
        {
            return listingAgent.toLowerCase().contains("profes") ? PROFESSIONAL : PRIVATE;
        }
        return null;
    }
}
