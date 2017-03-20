package com.idealista.scraper;

import com.idealista.scraper.data.xls.XlsExporter;
import com.idealista.scraper.model.parser.ISearchAttributesParser;
import com.idealista.scraper.model.parser.IdealistaSearchAttributesParser;
import com.idealista.scraper.model.parser.VibboSearchAttributesParser;
import com.idealista.scraper.scraping.category.ICategoriesChooser;
import com.idealista.scraper.scraping.category.IdealistaCategoriesChooser;
import com.idealista.scraper.scraping.category.VibboCategoriesChooser;
import com.idealista.scraper.scraping.paginator.IPaginator;
import com.idealista.scraper.scraping.paginator.IdealistaPaginator;
import com.idealista.scraper.scraping.paginator.VibboPaginator;
import com.idealista.scraper.service.IScrappingService;
import com.idealista.scraper.service.IdealistaScrappingService;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.service.VibboScrappingService;
import com.idealista.scraper.util.PropertiesLoader;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyProvider;

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

import java.io.IOException;

@Configuration
@ComponentScan
@PropertySources({@PropertySource("file:settings/realty.properties"),
        @PropertySource("file:settings/sources/${scrapTarget}.properties")})
public class AppConfig
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
        ctx.getEnvironment().setActiveProfiles(PropertiesLoader.getActiveProfile());
        ctx.register(AppConfig.class);
        ctx.refresh();
        RealtyApp app = ctx.getBean(RealtyApp.class);
        app.printInfo();
        constructBeans(ctx);
        app.run();
        ctx.close();
    }

    @Bean
    public static PropertySourcesPlaceholderConfigurer placeholderConfigurer()
    {
        return new PropertySourcesPlaceholderConfigurer();
    }

    private static void constructBeans(AnnotationConfigApplicationContext ctx)
    {
        ctx.getBean(RealtyApp.class).initSystemProperties();
        ctx.getBean(XlsExporter.class).initWorkBook();
        ctx.getBean(ProxyProvider.class).initProxiesList();
        ctx.getBean(WebDriverProvider.class).init();
    }

    @Autowired
    private Environment environment;

    @Value("#{ '${scrapTarget}'.toUpperCase() }")
    private ScrapTarget scrapTarget;

    @Bean
    public ICategoriesChooser categoriesChooser()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return new VibboCategoriesChooser();
            case IDEALISTA:
                return new IdealistaCategoriesChooser();
            default:
                throw illegalException("ICategoriesChooser");
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

    @Bean
    public IPaginator paginator()
    {
        switch (scrapTarget)
        {
            case VIBBO:
                return new VibboPaginator();
            case IDEALISTA:
                return new IdealistaPaginator();
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
            default:
                throw illegalException("ISearchAttributesParser");
        }
    }

    private IllegalArgumentException illegalException(String beanType)
    {
        return new IllegalArgumentException(
                "Could not determine " + beanType + " based on scrapTarget value: " + scrapTarget);
    }
}
