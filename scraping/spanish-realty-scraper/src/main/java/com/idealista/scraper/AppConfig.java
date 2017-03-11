package com.idealista.scraper;

import com.idealista.scraper.data.xls.XlsExporter;
import com.idealista.scraper.webdriver.WebDriverProvider;
import com.idealista.scraper.webdriver.proxy.ProxyProvider;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import java.io.IOException;

@Configuration
@ComponentScan
@PropertySource("file:realty.properties")
public class AppConfig
{
    public static void main(String[] args) throws InterruptedException, IOException
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext();
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
}
