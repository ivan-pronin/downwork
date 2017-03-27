package com.crunchbase.scraper;

import com.crunchbase.scraper.service.CrunchbaseScraperService;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

public class CrunchbaseApplication
{
    public static void main(String[] args)
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(CrunchbaseAppConfig.class);

        CrunchbaseScraperService service = ctx.getBean(CrunchbaseScraperService.class);
        service.scrap();
        ctx.close();
    }
}
