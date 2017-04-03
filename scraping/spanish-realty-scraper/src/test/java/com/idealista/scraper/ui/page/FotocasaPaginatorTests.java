package com.idealista.scraper.ui.page;

import java.net.URL;
import java.util.Set;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.paginator.FotocasaPaginator;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class FotocasaPaginatorTests
{
    @Autowired
    private FotocasaPaginator paginator;
    
    @Test
    public void testName() throws Exception
    {
        
        Category baseCategory = createCategoryFromUrl("http://www.fotocasa.es/es/comprar/casas/madrid-capital/todas-las-zonas/l?"
                + "latitude=40.4096&longitude=-3.6862&combinedLocationIds=724,14,28,173,0,28079,0,0,0");
        Set<Category> generatedUrls = paginator.getAllPageUrls(baseCategory);
        System.out.println(generatedUrls.size());
        generatedUrls.forEach(System.out::println);
        Assert.assertTrue( generatedUrls.size() > 500);
        
        baseCategory = createCategoryFromUrl("http://www.fotocasa.es/es/alquiler/casas/alicante-provincia/alt-vinalopo/l");
        generatedUrls = paginator.getAllPageUrls(baseCategory);
        System.out.println(generatedUrls.size());
        generatedUrls.forEach(System.out::println);
        Assert.assertTrue(generatedUrls.size() >= 1);
        
         baseCategory = createCategoryFromUrl("http://www.fotocasa.es/search/results.aspx?opi=36&ts=Madrid%20Capital&t=&llm=724,14,28,173,0,"
                + "28079,0,0,0&bti=2&bsm=&tti=1&mode=1&prchti=1&cu=es-es&minp=&maxp=&mins=&maxs=&minr=&minb=&esm=&csm=");
        generatedUrls = paginator.getAllPageUrls(baseCategory);
        System.out.println(generatedUrls.size());
        generatedUrls.forEach(System.out::println);
        Assert.assertTrue(generatedUrls.size() > 5);

    }
    
    private Category createCategoryFromUrl(String string) throws Exception
    {
        return new Category(new URL(string), null);
    }

    //@Test
    public void test2() throws Exception
    {
        String url = "http://www.fotocasa.es/es/comprar/casas/madrid-capital/todas-las-zonas/l?"
                + "latitude=40.4096&longitude=-3.6862&combinedLocationIds=724,14,28,173,0,28079,0,0,0";
        String[] parts = url.split("\\?latitude=");
        System.out.println(parts[0]);
        System.out.println(parts[1]);
    }
}
