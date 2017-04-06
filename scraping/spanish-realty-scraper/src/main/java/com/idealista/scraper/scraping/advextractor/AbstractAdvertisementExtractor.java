package com.idealista.scraper.scraping.advextractor;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.webdriver.INavigateActions;

public abstract class AbstractAdvertisementExtractor implements IAdvertisementExtractor
{
    private INavigateActions navigateActions;
    private Category category;

    protected AbstractAdvertisementExtractor(Category category)
    {
        this.category = category;
    }

    protected INavigateActions getNavigateActions()
    {
        return navigateActions;
    }

    protected void setNavigateActions(INavigateActions navigateActions)
    {
        this.navigateActions = navigateActions;
    }

    protected Category getCategory()
    {
        return category;
    }

    protected void setCategory(Category category)
    {
        this.category = category;
    }
}
