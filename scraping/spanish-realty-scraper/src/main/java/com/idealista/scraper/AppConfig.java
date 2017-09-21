package com.idealista.scraper;

import java.io.IOException;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.annotation.Scope;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

import com.idealista.scraper.model.parser.ISearchAttributesParser;
import com.idealista.scraper.model.parser.IdealistaSearchAttributesParser;
import com.idealista.scraper.model.parser.PisosSearchAttributesParser;
import com.idealista.scraper.model.parser.VibboSearchAttributesParser;
import com.idealista.scraper.scraping.advextractor.AbstractAdvertisementExtractor;
import com.idealista.scraper.scraping.advextractor.FotocasaAdvertisementExtractor;
import com.idealista.scraper.scraping.advextractor.IAdvertisementExtractor;
import com.idealista.scraper.scraping.advextractor.IdealistaAdvertisementExtractor;
import com.idealista.scraper.scraping.advextractor.PisosAdvertisementExtractor;
import com.idealista.scraper.scraping.advextractor.VibboAdvertisementExtractor;
import com.idealista.scraper.scraping.category.chooser.FotocasaCategoriesChooser;
import com.idealista.scraper.scraping.category.chooser.FotocasaSubDistrictChooser;
import com.idealista.scraper.scraping.category.chooser.ICategoriesChooser;
import com.idealista.scraper.scraping.category.chooser.IdealistaCategoriesChooser;
import com.idealista.scraper.scraping.category.chooser.PisosCategoriesChooser;
import com.idealista.scraper.scraping.category.chooser.VibboCategoriesChooser;
import com.idealista.scraper.scraping.category.filter.FotocasaAdUrlsFilter;
import com.idealista.scraper.scraping.category.filter.IAdUrlsFilter;
import com.idealista.scraper.scraping.category.filter.IdealistaAdUrlsFilter;
import com.idealista.scraper.scraping.category.filter.PisosAdUrlsFilter;
import com.idealista.scraper.scraping.category.provider.FotocasaCategoriesProvider;
import com.idealista.scraper.scraping.category.provider.ICategoriesProvider;
import com.idealista.scraper.scraping.category.provider.IdealistaCategoriesProvider;
import com.idealista.scraper.scraping.category.provider.PisosCategoriesProvider;
import com.idealista.scraper.scraping.category.provider.VibboCategoriesProvider;
import com.idealista.scraper.scraping.paginator.FotocasaPaginator;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.scraping.paginator.IdealistaPaginator;
import com.idealista.scraper.scraping.paginator.PisosPaginator;
import com.idealista.scraper.scraping.paginator.VibboPaginator;
import com.idealista.scraper.scraping.searchpage.processor.FotocasaSearchPageProcessor;
import com.idealista.scraper.scraping.searchpage.processor.ISeachPageProcessor;
import com.idealista.scraper.scraping.searchpage.processor.IdealistaSearchPageProcessor;
import com.idealista.scraper.scraping.searchpage.processor.PisosSearchPageProcessor;
import com.idealista.scraper.scraping.searchpage.processor.VibboSearchPageProcessor;
import com.idealista.scraper.service.FotocasaScrappingService;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.service.IdealistaScrappingService;
import com.idealista.scraper.service.PisosScrappingService;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.service.VibboScrappingService;
import com.idealista.scraper.ui.page.IAdvertisementPage;
import com.idealista.scraper.ui.page.IMapPage;
import com.idealista.scraper.ui.page.ISearchPage;
import com.idealista.scraper.ui.page.IStartPage;
import com.idealista.scraper.ui.page.advertisement.FotocasaAdvertisementPage;
import com.idealista.scraper.ui.page.advertisement.IdealistaAdvertisementPage;
import com.idealista.scraper.ui.page.advertisement.PisosAdvertisementPage;
import com.idealista.scraper.ui.page.advertisement.VibboAdvertisementPage;
import com.idealista.scraper.ui.page.fotocasa.FotocasaStartPage;
import com.idealista.scraper.ui.page.idealista.IdealistaMapPage;
import com.idealista.scraper.ui.page.idealista.IdealistaSearchPage;
import com.idealista.scraper.ui.page.idealista.IdealistaStartPage;
import com.idealista.scraper.ui.page.pisos.PisosMapPage;
import com.idealista.scraper.ui.page.pisos.PisosSearchPage;
import com.idealista.scraper.ui.page.pisos.PisosStartPage;
import com.idealista.scraper.ui.page.vibbo.VibboStartPage;
import com.idealista.scraper.util.PropertiesLoader;

@Configuration
@ComponentScan
@PropertySources({@PropertySource("file:settings/realty.properties"),
        @PropertySource("file:settings/sources/${scrapTarget}.properties")})
public class AppConfig
{
    @Autowired
    private Environment environment;

    @Value("#{ '${scrapTarget}'.toUpperCase() }")
    private ScrapTarget scrapTarget;

    @Value("${privateAdsFiltering}")
    private boolean privateAdsFiltering;

    public static void main(String[] args) throws InterruptedException, IOException
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles(PropertiesLoader.getActiveProfile());
        ctx.register(AppConfig.class);
        ctx.refresh();
        RealtyApp app = ctx.getBean(RealtyApp.class);
        app.printInfo();
        app.run();
        ctx.close();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

    @Bean
    @Scope("prototype")
    public FotocasaSubDistrictChooser fotocasaSubDistrictChooser()
    {
        return new FotocasaSubDistrictChooser();
    }

    @Bean
    public Supplier<FotocasaSubDistrictChooser> fotocasaSubDistrictChooserSupplier()
    {
        return this::fotocasaSubDistrictChooser;
    }

    @Bean
    @Scope("prototype")
    public ICategoriesChooser categoriesChooser()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return new VibboCategoriesChooser();
            case IDEALISTA:
                return new IdealistaCategoriesChooser();
            case FOTOCASA:
                return new FotocasaCategoriesChooser();
            case PISOS:
                return new PisosCategoriesChooser();
            default:
                throw illegalException("ICategoriesChooser");
        }
    }

    @Bean
    public Supplier<ICategoriesChooser> categoriesChooserSupplier()
    {
        return this::categoriesChooser;
    }

    @Bean
    @Scope("prototype")
    public IStartPage startPage()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return new VibboStartPage();
            case IDEALISTA:
                return new IdealistaStartPage();
            case FOTOCASA:
                return new FotocasaStartPage();
            case PISOS:
                return new PisosStartPage();
            default:
                throw illegalException("IStartPage");
        }
    }

    @Bean
    public Supplier<IStartPage> startPageSupplier()
    {
        return this::startPage;
    }

    @Bean
    @Scope("prototype")
    public ISearchPage searchPage()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                throw illegalException("IStartPage");
            case IDEALISTA:
                return new IdealistaSearchPage();
            case FOTOCASA:
                throw illegalException("IStartPage");
            case PISOS:
                return new PisosSearchPage();
            default:
                throw illegalException("IStartPage");
        }
    }

    @Bean
    public Supplier<ISearchPage> searchPageSupplier()
    {
        return this::searchPage;
    }

    @Bean
    @Scope("prototype")
    public IMapPage mapPage()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return null;
            case IDEALISTA:
                return new IdealistaMapPage();
            case FOTOCASA:
                return null;
            case PISOS:
                return new PisosMapPage();
            default:
                throw illegalException("IMapPage");
        }
    }

    @Bean
    public Supplier<IMapPage> mapPageSupplier()
    {
        return this::mapPage;
    }

    @Bean
    @Scope("prototype")
    public IAdvertisementPage advertisementPage()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return new VibboAdvertisementPage();
            case IDEALISTA:
                return new IdealistaAdvertisementPage();
            case FOTOCASA:
                return new FotocasaAdvertisementPage();
            case PISOS:
                return new PisosAdvertisementPage();
            default:
                throw illegalException("IAdvertisementPage");
        }
    }

    @Bean
    public Supplier<IAdvertisementPage> advertisementPageSupplier()
    {
        return this::advertisementPage;
    }

    @Bean
    public ICategoriesProvider categoriesProvider()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return new VibboCategoriesProvider();
            case IDEALISTA:
                return new IdealistaCategoriesProvider();
            case FOTOCASA:
                return new FotocasaCategoriesProvider();
            case PISOS:
                return new PisosCategoriesProvider();
            default:
                throw illegalException("ICategoriesChooser");
        }
    }

    @Bean
    @Scope("prototype")
    public IAdvertisementExtractor advertisementExtractor()
    {
        switch (scrapTarget)
        {
            case IDEALISTA:
                AbstractAdvertisementExtractor extractor = new IdealistaAdvertisementExtractor();
                ((IdealistaAdvertisementExtractor) extractor).setLanguage(getLanguage());
                return extractor;
            case VIBBO:
                return new VibboAdvertisementExtractor();
            case FOTOCASA:
                return new FotocasaAdvertisementExtractor();
            case PISOS:
                return new PisosAdvertisementExtractor();
            default:
                throw illegalException("IAdvertisementExtractor");
        }
    }

    @Bean
    public Supplier<IAdvertisementExtractor> advertisementExtractorSupplier()
    {
        return this::advertisementExtractor;
    }

    @Bean
    @Scope("prototype")
    public ISeachPageProcessor seachPageProcessor()
    {
        switch (scrapTarget)
        {
            case IDEALISTA:
                return new IdealistaSearchPageProcessor();
            case VIBBO:
                return new VibboSearchPageProcessor();
            case FOTOCASA:
                return new FotocasaSearchPageProcessor();
            case PISOS:
                return new PisosSearchPageProcessor();
            default:
                throw illegalException("ISeachPageProcessor");
        }
    }

    @Bean
    public Supplier<ISeachPageProcessor> seachPageProcessorSupplier()
    {
        return this::seachPageProcessor;
    }

    @Bean
    @Scope("prototype")
    public IAdUrlsFilter adUrlsFilter()
    {
        switch (scrapTarget)
        {
            case IDEALISTA:
                return new IdealistaAdUrlsFilter();
            case VIBBO:
                return null;
            case FOTOCASA:
                return new FotocasaAdUrlsFilter();
            case PISOS:
                return new PisosAdUrlsFilter();
            default:
                throw illegalException("IAdUrlsFilter");
        }
    }

    @Bean
    public Supplier<IAdUrlsFilter> adUrlsFilterSupplier()
    {
        return this::adUrlsFilter;
    }

    @Bean
    public IPaginator paginator()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return new VibboPaginator();
            case IDEALISTA:
                return new IdealistaPaginator();
            case FOTOCASA:
                return new FotocasaPaginator();
            case PISOS:
                return new PisosPaginator();
            default:
                throw illegalException("IPaginator");
        }
    }

    @Bean
    public IScrappingService scrappingService()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return new VibboScrappingService();
            case IDEALISTA:
                return new IdealistaScrappingService();
            case FOTOCASA:
                return new FotocasaScrappingService();
            case PISOS:
                return new PisosScrappingService();
            default:
                throw illegalException("IScrappingService");
        }
    }

    @Bean
    public ISearchAttributesParser searchAttributesParser()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return new VibboSearchAttributesParser();
            case IDEALISTA:
                return new IdealistaSearchAttributesParser();
            case FOTOCASA:
                return null;
            case PISOS:
                return new PisosSearchAttributesParser();
            default:
                throw illegalException("ISearchAttributesParser");
        }
    }

    public String getLanguage()
    {
        if (scrapTarget.equals(ScrapTarget.IDEALISTA))
        {
            return environment.getProperty("language");
        }
        throw new UnsupportedOperationException("GetLanguage method is not supported by this configuration");
    }

    public ScrapTarget getScrapTarget()
    {
        return scrapTarget;
    }

    private IllegalArgumentException illegalException(String beanType)
    {
        return new IllegalArgumentException(
                "Could not determine " + beanType + " based on scrapTarget value: " + scrapTarget);
    }

    public boolean isPrivateAdsFiltering()
    {
        return privateAdsFiltering;
    }
}
