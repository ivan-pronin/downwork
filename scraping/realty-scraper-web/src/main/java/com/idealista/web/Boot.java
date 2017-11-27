package com.idealista.web;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.RealtyApp;
import com.idealista.web.config.BaseScraperConfiguration;
import com.idealista.web.config.BaseSourceConfiguration;
import com.idealista.web.config.IdealistaSourceConfiguration;
import com.idealista.web.config.ScraperConfiguration;
import com.idealista.web.controller.StorageProperties;

@SpringBootApplication
@Import({AppConfig.class})
@EnableConfigurationProperties(StorageProperties.class)
public class Boot
{
    private static ConfigurableApplicationContext webContext;

    @Autowired
    private BaseScraperConfiguration scraperConfig;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        webContext = SpringApplication.run(Boot.class);
        // ctx = SpringApplication.run(AppConfig.class, args);
        // ctx.getEnvironment().setActiveProfiles(PropertiesLoader.getActiveProfile());
        // RealtyApp app = ctx.getBean(RealtyApp.class);
        // app.run();
    }

    public void launch()
    {
        // AnnotationConfigApplicationContext appContext = new AnnotationConfigApplicationContext();
        // appContext.setParent(webContext);
        // appContext.register(AppConfig.class);
        // appContext.refresh();
        // appContext.start();

        RealtyApp app = webContext.getBean(RealtyApp.class);
        app.printBootContextData();
        try
        {
            app.printInfo();
            app.run();
        }
        catch (InterruptedException | IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally
        {

        }
        app.printBootContextData();
        System.out.println("APP IS finished!");
    }

    @Bean
    public BaseScraperConfiguration baseScraperConfiguration()
    {
        return new ScraperConfiguration();
    }

    @Bean
    public BaseSourceConfiguration baseSourceConfiguration()
    {
        return new IdealistaSourceConfiguration();
    }
}
