package com.idealista.web;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
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
    private static final Logger LOGGER = LogManager.getLogger(Boot.class);
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

    @Bean
    public CommandLineRunner demo(CustomerRepository repository)
    {
        return (args) ->
        {
            // save a couple of customers
            repository.save(new Customer("Jack", "Bauer"));
            repository.save(new Customer("Chloe", "O'Brian"));
            repository.save(new Customer("Kim", "Bauer"));
            repository.save(new Customer("David", "Palmer"));
            repository.save(new Customer("Michelle", "Dessler"));

            // fetch all customers
            LOGGER.info("Customers found with findAll():");
            LOGGER.info("-------------------------------");
            for (Customer customer : repository.findAll())
            {
                LOGGER.info(customer.toString());
            }
            LOGGER.info("");

            // fetch an individual customer by ID
            Customer customer = repository.findOne(1L);
            LOGGER.info("Customer found with findOne(1L):");
            LOGGER.info("--------------------------------");
            LOGGER.info(customer.toString());
            LOGGER.info("");

            // fetch customers by last name
            LOGGER.info("Customer found with findByLastName('Bauer'):");
            LOGGER.info("--------------------------------------------");
            for (Customer bauer : repository.findByLastName("Bauer"))
            {
                LOGGER.info(bauer.toString());
            }
            LOGGER.info("");
        };
    }
}
