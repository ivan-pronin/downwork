package com.idealista.scraper.scraping;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchPageProcessorFactory implements ISearchPageProcessorFactory
{
    @Autowired
    private WebDriverProvider webDriverProvider;
    
    @Autowired
    private ProxyMonitor proxyMonitor;

    @Override
    public SearchPageProcessor create(Category category)
    {
        return new SearchPageProcessor(category, webDriverProvider, proxyMonitor);
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }

    public void setProxyMonitor(ProxyMonitor proxyMonitor)
    {
        this.proxyMonitor = proxyMonitor;
    }
}
