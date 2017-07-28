package com.idealista.scraper.vibbo;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.service.IScrappingService;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ContextConfiguration(classes = AppConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class VibboPrivateFilteringTests
{   
    
    @Autowired
    private IScrappingService service;
    
    @Test
    public void testName() throws Exception
    {
        service.scrapSite();
    }
}
