package com.idealista.scraper.ui.page;

import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.ui.actions.ClickActions;
import com.idealista.scraper.ui.actions.SearchActions;
import com.idealista.scraper.ui.actions.WaitActions;

public abstract class BasePage
{
    protected static final String TODO = "// TODO";
    protected static final String INFO_NOT_FOUND = "Info not found";
    protected static final String NA_FOR_THIS_SITE = "N/A for this site";
    protected static final String PRIVATE = "Private";
    protected static final String PROFESSIONAL = "Professional";

    @Autowired
    protected SearchActions searchActions;

    @Autowired
    protected ClickActions clickActions;

    @Autowired
    protected WaitActions waitActions;

    protected String isProfessional(String listingAgent)
    {
        if (listingAgent != null)
        {
            return listingAgent.toLowerCase().contains("profes") ? PROFESSIONAL : PRIVATE;
        }
        return null;
    }
}
