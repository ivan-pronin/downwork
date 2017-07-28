package com.idealista.scraper.scraping.searchpage.factory;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.category.filter.FotocasaAdUrlsFilter;
import com.idealista.scraper.scraping.category.filter.IAdUrlsFilter;
import com.idealista.scraper.scraping.category.filter.IdealistaAdUrlsFilter;
import com.idealista.scraper.scraping.category.filter.PisosAdUrlsFilter;
import com.idealista.scraper.scraping.searchpage.processor.AbstractSearchPageProcessor;
import com.idealista.scraper.scraping.searchpage.processor.FotocasaSearchPageProcessor;
import com.idealista.scraper.scraping.searchpage.processor.ISeachPageProcessor;
import com.idealista.scraper.scraping.searchpage.processor.IdealistaSearchPageProcessor;
import com.idealista.scraper.scraping.searchpage.processor.PisosSearchPageProcessor;
import com.idealista.scraper.scraping.searchpage.processor.VibboSearchPageProcessor;
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
        IAdUrlsFilter adUrlsFilter = null;
        switch (appConfig.getScrapTarget())
        {
            case IDEALISTA:
                processor = new IdealistaSearchPageProcessor(category);
                adUrlsFilter = new IdealistaAdUrlsFilter();
                break;
            case VIBBO:
                processor = new VibboSearchPageProcessor(category);
                break;
            case FOTOCASA:
                processor = new FotocasaSearchPageProcessor(category);
                adUrlsFilter = new FotocasaAdUrlsFilter();
                break;
            case PISOS:
                processor = new PisosSearchPageProcessor(category);
                adUrlsFilter = new PisosAdUrlsFilter();
                break;
            default:
                throw new IllegalArgumentException("Could not create necessary ISearchPageProcessor object");
        }
        processor.setNavigateActions(navigateActions);
        processor.setProxyMonitor(proxyMonitor);
        processor.setWebDriverProvider(webDriverProvider);
        processor.setAdUrlsFilter(adUrlsFilter);
        processor.setApplyFilter(appConfig.isPrivateAdsFiltering());
        return processor;
    }
}
