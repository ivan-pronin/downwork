package com.idealista.scraper;

import java.io.IOException;

import com.idealista.scraper.model.parser.ISearchAttributesParser;
import com.idealista.scraper.model.parser.IdealistaSearchAttributesParser;
import com.idealista.scraper.model.parser.PisosSearchAttributesParser;
import com.idealista.scraper.model.parser.VibboSearchAttributesParser;
import com.idealista.scraper.scraping.category.FotocasaCategoriesChooser;
import com.idealista.scraper.scraping.category.ICategoriesChooser;
import com.idealista.scraper.scraping.category.IdealistaCategoriesChooser;
import com.idealista.scraper.scraping.category.PisosCategoriesChooser;
import com.idealista.scraper.scraping.category.VibboCategoriesChooser;
import com.idealista.scraper.scraping.paginator.FotocasaPaginator;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.scraping.paginator.IdealistaPaginator;
import com.idealista.scraper.scraping.paginator.PisosPaginator;
import com.idealista.scraper.scraping.paginator.VibboPaginator;
import com.idealista.scraper.service.FotocasaScrappingService;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.service.IdealistaScrappingService;
import com.idealista.scraper.service.PisosScrappingService;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.service.VibboScrappingService;
import com.idealista.scraper.util.PropertiesLoader;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.env.Environment;

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
                illegalException("IScrappingService");
        }
        return null;
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
}
