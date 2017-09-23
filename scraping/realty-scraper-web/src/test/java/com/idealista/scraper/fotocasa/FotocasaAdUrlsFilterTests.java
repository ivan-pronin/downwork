package com.idealista.scraper.fotocasa;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.scraping.searchpage.processor.ISeachPageProcessor;

@ContextConfiguration(classes = AppConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class FotocasaAdUrlsFilterTests
{

    @Autowired
    private Supplier<ISeachPageProcessor> seachPageProcessorSupplier;

    @Test
    public void testName() throws Exception
    {
        List<Category> categories = createCategories();
        Set<Category> foundCategories = new HashSet<>();
        for (Category cat : categories)
        {
            ISeachPageProcessor processor = seachPageProcessorSupplier.get();
            foundCategories.addAll(processor.call());
        }
        Assert.assertEquals(3, foundCategories.size());
    }

    private List<Category> createCategories() throws MalformedURLException
    {
        return Arrays.asList(
                new Category(new URL("https://www.fotocasa.es/es/comprar/casas/madrid-capital/todas-las-zonas/l"),
                        null),
                new Category(new URL(
                        "https://www.fotocasa.es/comprar/obra-nueva/madrid-capital/listado-por-foto?crp=2&ts=madrid%20capital,%20madrid&llm=724,14,28,173,0,28079,0,0,0&opi=36&ftg=false&pgg=true&odg=false&fav=false&grad=false&fss=false&mode=1&cu=es-es&craap=1&fs=false"),
                        null),
                new Category(new URL(
                        "https://www.fotocasa.es/comprar/obra-nueva/madrid-capital/listado?crp=2&ts=madrid%20capital,%20madrid&llm=724,14,28,173,0,28079,0,0,0&opi=36&ftg=false&pgg=true&odg=false&fav=false&grad=false&fss=false&mode=3&cu=es-es&craap=1&fs=false&tta=0"),
                        null));
    }
}
