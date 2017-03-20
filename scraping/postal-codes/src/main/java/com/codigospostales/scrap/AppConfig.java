package com.codigospostales.scrap;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@ComponentScan
@Configuration
public class AppConfig
{
    public static void main(String[] args)
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(AppConfig.class);
        PostalCodesService service = ctx.getBean(PostalCodesService.class);
        service.scrapPostalCodes();
        
        ctx.close();
    }
    
    @Bean
    public WebDriver webDriver()
    {
        return new ChromeDriver(DesiredCapabilities.chrome());
    }
}
