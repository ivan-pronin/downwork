package com.idealista.scraper.scraping.advextractor;

import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.webdriver.INavigateActions;

public abstract class AbstractAdvertisementExtractor implements IAdvertisementExtractor
{
    @Autowired
    private INavigateActions navigateActions;

    private Category category;

    protected INavigateActions getNavigateActions()
    {
        return navigateActions;
    }

    protected Category getCategory()
    {
        return category;
    }

    public void setCategory(Category category)
    {
        this.category = category;
    }
}
