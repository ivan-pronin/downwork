package com.idealista.scraper.scraping.searchpage.processor;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.category.filter.IAdUrlsFilter;
import com.idealista.scraper.ui.actions.ClickActions;
import com.idealista.scraper.ui.actions.SearchActions;
import com.idealista.scraper.ui.actions.WaitActions;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

import org.openqa.selenium.WebDriver;

public abstract class AbstractSearchPageProcessor implements ISeachPageProcessor
{
    private Category category;
    private WebDriverProvider webDriverProvider;
    private INavigateActions navigateActions;
    private ProxyMonitor proxyMonitor;
    private SearchActions searchActions = new SearchActions();
    private ClickActions clickActions = new ClickActions();
    private WaitActions waitActions = new WaitActions();
    private IAdUrlsFilter adUrlsFilter;
    
    protected boolean applyFilter;

    public AbstractSearchPageProcessor(Category category)
    {
        this.category = category;
    }

    protected Category getCategory()
    {
        return category;
    }

    protected void setCategory(Category category)
    {
        this.category = category;
    }

    protected WebDriverProvider getWebDriverProvider()
    {
        return webDriverProvider;
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }

    protected INavigateActions getNavigateActions()
    {
        return navigateActions;
    }

    public void setNavigateActions(INavigateActions navigateActions)
    {
        this.navigateActions = navigateActions;
    }

    protected ProxyMonitor getProxyMonitor()
    {
        return proxyMonitor;
    }

    public void setProxyMonitor(ProxyMonitor proxyMonitor)
    {
        this.proxyMonitor = proxyMonitor;
    }

    protected void setWebDriver(WebDriver driver)
    {
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
        waitActions.setWebDriver(driver);
        adUrlsFilter.setSearchActions(searchActions);
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

    public void setAdUrlsFilter(IAdUrlsFilter adUrlsFilter)
    {
        this.adUrlsFilter = adUrlsFilter;
    }

    public void setApplyFilter(boolean applyFilter)
    {
        this.applyFilter = applyFilter;
    }
}
