package com.idealista.scraper.service;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.function.Supplier;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.idealista.scraper.executor.ExecutorServiceProvider;
import com.idealista.scraper.model.Advertisement;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.parser.ISearchAttributesParser;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.PisosSearchAttributes;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.scraping.advextractor.AbstractAdvertisementExtractor;
import com.idealista.scraper.scraping.advextractor.IAdvertisementExtractor;
import com.idealista.scraper.scraping.category.AdUrlsFinder;
import com.idealista.scraper.scraping.category.FoundUrlsManager;
import com.idealista.scraper.scraping.category.IAdUrlsFinder;
import com.idealista.scraper.scraping.category.provider.ICategoriesProvider;
import com.idealista.scraper.webdriver.INavigateActions;

@Profile("pisos")
@Component
public class PisosScrappingService implements IScrappingService
{
    public static final String NOT_SPECIFIED = "NOT_SPECIFIED";
    private static final Logger LOGGER = LogManager.getLogger(PisosScrappingService.class);
    private static final ScrapTarget SCRAP_TARGET = ScrapTarget.PISOS;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${operation}') }")
    private Set<String> userOperation;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${typology}') }")
    private Set<String> userTypology;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${location}') }")
    private Set<String> userLocation;

    @Value("#{ T(java.util.Arrays).asList('${zone}') }")
    private List<String> zone;

    @Value("#{ T(java.util.Arrays).asList('${municipio}') }")
    private List<String> municipio;

    @Value("#{ T(java.util.Arrays).asList('${extras}') }")
    private List<String> extras;

    @Value("#{ T(org.springframework.util.StringUtils).delimitedListToStringArray('${distro}',';') }")
    private List<String> distro;

    @Value(value = "${maxAdsToProcess}")
    private int maxAdsToProcess;

    @Autowired
    private ExecutorServiceProvider executor;

    @Autowired
    private IAdUrlsFinder adUrlsFinder;

    @Autowired
    private INavigateActions navigateActions;

    @Autowired
    private ISearchAttributesParser searchAttributesParser;

    @Autowired
    private ICategoriesProvider categoriesChooser;

    @Autowired
    private FoundUrlsManager foundUrlsManager;

    @Autowired
    private Supplier<IAdvertisementExtractor> advertismentExtractorSupplier;

    private BlockingQueue<Future<Advertisement>> advertismentExtractorResults;
    private BlockingQueue<URL> advertismentUrlsInProgress = new LinkedBlockingQueue<>();

    @Override
    public void scrapSite() throws InterruptedException, MalformedURLException
    {
        navigateActions.get(new URL(SCRAP_TARGET.getMainPageUrl()));
        SearchAttributes searchAttributes = searchAttributesParser.parseSearchAttributes(getSearchAttributesData());
        List<Map<String, List<String>>> filterAttributes = getFilterAttributesData();
        GenericSearchFilterContext context = new GenericSearchFilterContext();
        context.setSearchAttributes(searchAttributes);
        context.setGenericFilterAttributes(filterAttributes);
        LOGGER.info(" === === Printing parsed SearchAttributes === === ");
        LOGGER.info(searchAttributes);
        LOGGER.info(" === === === === === === ===  === === === === === ");
        LOGGER.info("");

        Set<Category> categoriesBaseUrls = categoriesChooser.getCategoriesUrls(context);

        ((AdUrlsFinder) adUrlsFinder).setCategoriesBaseUrls(categoriesBaseUrls);
        Set<Category> adUrlsToProcess = adUrlsFinder.findNewAdUrlsAmount(maxAdsToProcess);

        adUrlsToProcess = foundUrlsManager.getNewestAdsById(adUrlsToProcess);

        LOGGER.debug("Printing all URLS that will be scrapped in this session. Total count: {}",
                adUrlsToProcess.size());
        adUrlsToProcess.forEach(e -> LOGGER.debug(e.getUrl()));

        Iterator<Category> iterator = adUrlsToProcess.iterator();
        for (int i = 0; i < maxAdsToProcess; i++)
        {
            if (iterator.hasNext())
            {
                Category page = iterator.next();
                IAdvertisementExtractor iAdvertisementExtractor = advertismentExtractorSupplier.get();
                ((AbstractAdvertisementExtractor) iAdvertisementExtractor).setCategory(page);
                advertismentExtractorResults.put(executor.getExecutor().submit(iAdvertisementExtractor));
                advertismentUrlsInProgress.put(page.getUrl());
            }
        }
    }

    private List<Map<String, List<String>>> getFilterAttributesData()
    {
        checkFilterAttributesInputData();
        List<Map<String, List<String>>> data = new ArrayList<>();
        for (String zoneItem : zone)
        {
            data.add(createOneFilterSet(zoneItem, municipio.iterator().next(), distro.iterator().next(),
                    extras.iterator().hasNext() ? extras.iterator().next() : ""));
        }
        // The case when no data is specified in parameters. We still need at least on 'distro' value
        // to launch categories chooser
        if (data.isEmpty())
        {
            data.add(createOneFilterSet(NOT_SPECIFIED, NOT_SPECIFIED, NOT_SPECIFIED,
                    extras.iterator().hasNext() ? extras.iterator().next() : ""));
        }
        return data;
    }

    private Map<String, List<String>> createOneFilterSet(String zoneValue, String municipioValue, String distrosValue,
            String extrasValue)
    {
        Map<String, List<String>> dataset = new HashMap<>();
        dataset.put("zone", Arrays.asList(zoneValue));
        dataset.put("municipio", Arrays.asList(municipioValue));
        dataset.put("distro", Arrays.asList(distrosValue.split(",")));
        dataset.put("extras", Arrays.asList(extrasValue));
        return dataset;
    }

    private void checkFilterAttributesInputData()
    {
        if (zone.size() != municipio.size() || municipio.size() != distro.size())
        {
            throw new IllegalArgumentException("Invalid number of parameters for zones: " + zone.size()
                    + ", municipio: " + municipio.size() + ", distro: " + distro.size());
        }
    }

    private Map<IGenericSearchAttributes, Set<String>> getSearchAttributesData()
    {
        return new HashMap<IGenericSearchAttributes, Set<String>>()
        {
            private static final long serialVersionUID = -4234938230147805771L;
            {
                put(PisosSearchAttributes.OPERATION, userOperation);
                put(PisosSearchAttributes.TYPOLOGY, userTypology);
                put(PisosSearchAttributes.LOCATION, userLocation);
            }
        };
    }

    @Override
    public void setAdvertismentExtractorResults(BlockingQueue<Future<Advertisement>> advertismentExtractorResults)
    {
        this.advertismentExtractorResults = advertismentExtractorResults;
    }

    public BlockingQueue<URL> getAdvertismentUrlsInProgress()
    {
        return advertismentUrlsInProgress;
    }
}
