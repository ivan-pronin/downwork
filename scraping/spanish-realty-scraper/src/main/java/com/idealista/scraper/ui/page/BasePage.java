package com.idealista.scraper.ui.page;

import com.idealista.scraper.ui.ClickActions;
import com.idealista.scraper.ui.SearchActions;

import org.openqa.selenium.WebDriver;

public abstract class BasePage
{
    protected static final String TODO = "// TODO";
    protected static final String INFO_NOT_FOUND = "Info not found";
    protected static final String INFO_NOT_PRESENT = "N/A for this site";

    protected static final String PRIVATE = "Private";
    private static final String PROFESSIONAL = "Professional";

    protected SearchActions searchActions = new SearchActions();
    protected ClickActions clickActions = new ClickActions();

    public void setWebDriver(WebDriver driver)
    {
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
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
