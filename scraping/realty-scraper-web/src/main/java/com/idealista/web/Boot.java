package com.idealista.web;

import java.io.IOException;

import javax.sql.DataSource;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

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
    private static final Logger LOGGER = LogManager.getLogger(Boot.class);
    private static ConfigurableApplicationContext webContext;

    @Autowired
    private BaseScraperConfiguration scraperConfig;

    public static void main(String[] args) throws IOException, InterruptedException
    {
        webContext = SpringApplication.run(Boot.class);
        TestBean ttt = webContext.getBean(TestBean.class);
        ttt.testDb();
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

    @Value("${db.url}")
    private String dbUrl;

    @Value("${db.driverClassName}")
    private String driverClassName;

    @Value("${db.username}")
    private String userName;

    @Value("${db.password}")
    private String password;

    @Bean
    public DataSource dataSource()
    {
        DriverManagerDataSource dataSource = new DriverManagerDataSource(dbUrl, userName, password);
        dataSource.setDriverClassName(driverClassName);
        System.out.println("URL: " + dbUrl);
        System.out.println("username: " + userName);
        System.out.println("password: " + password);
        System.out.println("driver: " + driverClassName);
        return dataSource;
    }
}
