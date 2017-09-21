package com.idealista.scraper;

import java.net.URL;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.paginator.IPaginator;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {AppConfig.class})
public class FotocasaPaginatorTests
{

    @Autowired
    private IPaginator paginator;

    @Test
    public void testName() throws Exception
    {
        String baseUrl = "http://www.fotocasa.es/es/comprar/casas/elche-elx/valverde/l?latitude=40.4096&longitude=-3.6862&combinedLocationIds=724,14,28,173,0,28079,0,176,112;724,14,28,173,0,28079,0,176,113;724,14,28,173,0,28079,0,176,117;724,14,28,173,0,28079,0,176,115;724,14,28,173,0,28079,0,176,114;724,14,28,173,0,28079,0,176,116";
        Category cat = new Category();
        cat.setUrl(new URL(baseUrl));
        paginator.getAllPageUrls(cat);
    }
}
