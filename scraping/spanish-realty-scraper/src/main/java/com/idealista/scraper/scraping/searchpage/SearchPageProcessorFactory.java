package com.idealista.scraper.scraping.searchpage;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.webdriver.INavigateActions;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyMonitor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SearchPageProcessorFactory implements ISearchPageProcessorFactory
{
    @Autowired
    private AppConfig appConfig;

    @Autowired
    private WebDriverProvider webDriverProvider;

    @Autowired
    private ProxyMonitor proxyMonitor;

    @Autowired
    private INavigateActions navigateActions;

    @Override
    public ISeachPageProcessor create(Category category)
    {
        switch (appConfig.getScrapTarget())
        {
            case IDEALISTA:
                IdealistaSearchPageProcessor idealistaProcessor = new IdealistaSearchPageProcessor(category);
                idealistaProcessor.setNavigateActions(navigateActions);
                idealistaProcessor.setProxyMonitor(proxyMonitor);
                idealistaProcessor.setWebDriverProvider(webDriverProvider);
                return idealistaProcessor;
            case VIBBO:
                VibboSearchPageProcessor vibboProcessor = new VibboSearchPageProcessor(category);
                vibboProcessor.setNavigateActions(navigateActions);
                vibboProcessor.setProxyMonitor(proxyMonitor);
                vibboProcessor.setWebDriverProvider(webDriverProvider);
                return vibboProcessor;
            default:
                throw new IllegalArgumentException("Could not create necessary ISearchPageProcessor object");
        }
    }
}
