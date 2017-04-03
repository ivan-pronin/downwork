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
        AbstractSearchPageProcessor processor = null;
        switch (appConfig.getScrapTarget())
        {
            case IDEALISTA:
                processor = new IdealistaSearchPageProcessor(category);
                break;
            case VIBBO:
                processor = new VibboSearchPageProcessor(category);
                break;
            case FOTOCASA:
                processor = new FotocasaSearchPageProcessor(category);
                break;
            default:
                throw new IllegalArgumentException("Could not create necessary ISearchPageProcessor object");
        }
        processor.setNavigateActions(navigateActions);
        processor.setProxyMonitor(proxyMonitor);
        processor.setWebDriverProvider(webDriverProvider);
        return processor;
    }
}
