package com.idealista.scraper.scraping.searchpage;

import com.idealista.scraper.model.Category;
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

    protected void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }

    protected INavigateActions getNavigateActions()
    {
        return navigateActions;
    }

    protected void setNavigateActions(INavigateActions navigateActions)
    {
        this.navigateActions = navigateActions;
    }

    protected ProxyMonitor getProxyMonitor()
    {
        return proxyMonitor;
    }

    protected void setProxyMonitor(ProxyMonitor proxyMonitor)
    {
        this.proxyMonitor = proxyMonitor;
    }

    protected void setWebDriver(WebDriver driver)
    {
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
        waitActions.setWebDriver(driver);
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
}
