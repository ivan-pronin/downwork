package com.idealista.scraper.idealista;

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
public class IdealistaAdUrlsFilterTests
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
                new Category(new URL("https://www.idealista.com/en/alquiler-viviendas/madrid-provincia/"), null),
                new Category(new URL("https://www.idealista.com/en/alquiler-viviendas/madrid-provincia/pagina-4.htm"),
                        null));
    }
}
