package com.idealista.scraper.scraping.searchpage.processor;

import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.category.filter.IAdUrlsFilter;
import com.idealista.scraper.ui.actions.ClickActions;
import com.idealista.scraper.ui.actions.SearchActions;
import com.idealista.scraper.ui.actions.WaitActions;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

public abstract class AbstractSearchPageProcessor implements ISeachPageProcessor
{
    private Category category;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private ProxyMonitor proxyMonitor;

    @Autowired
    private SearchActions searchActions;

    @Autowired
    private ClickActions clickActions;

    @Autowired
    private WaitActions waitActions;

    @Autowired
    private IAdUrlsFilter adUrlsFilter;

    protected boolean applyFilter;

    protected Category getCategory()
    {
        return category;
    }

    public void setPage(Category category)
    {
        this.category = category;
    }

    protected WebDriverProvider getWebDriverProvider()
    {
        return webDriverProvider;
    }

    protected INavigateActions getNavigateActions()
    {
        return navigateActions;
    }

    protected ProxyMonitor getProxyMonitor()
    {
        return proxyMonitor;
    }

    protected SearchActions getSearchActions()
    {
        return searchActions;
    }

    protected ClickActions getClickActions()
    {
        return clickActions;
    }

    protected WaitActions getWaitActions()
    {
        return waitActions;
    }

    protected IAdUrlsFilter getAdUrlsFilter()
    {
        return adUrlsFilter;
    }
}
