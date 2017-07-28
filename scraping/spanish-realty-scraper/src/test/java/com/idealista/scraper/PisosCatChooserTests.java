package com.idealista.scraper;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.scraping.category.chooser.ICategoriesChooser;
import com.idealista.scraper.webdriver.WebDriverProvider;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = AppConfig.class)
public class PisosCatChooserTests
{
    @Autowired
    private ICategoriesChooser chooser;
    
    @Autowired
    private WebDriverProvider webDriverProvider;

    @Test
    public void testName() throws Exception
    {
        webDriverProvider.get().navigate().to("https://www.pisos.com/");
        GenericSearchFilterContext context = new GenericSearchFilterContext();
        context.setSearchAttributes(getSearchAttributesData());
        context.setGenericFilterAttributes(getGenericFilterAttributes());
        chooser.getCategoriesUrls(context).forEach(System.out::println);;
    }

    private List<Map<String, List<String>>> getGenericFilterAttributes()
    {
        List<Map<String, List<String>>> data = new ArrayList<>();
        data.add(ImmutableMap.of("zone", Arrays.asList("Alt Penedès"), "municipio", Arrays.asList("Castellet i la Gornal"),
                "distro", Arrays.asList("Castellet i la Gornal"), "extras", Arrays.asList("Última semana")));
        return data;
    }

    private SearchAttributes getSearchAttributesData()
    {
        return new SearchAttributes(new HashSet<>(Arrays.asList("Comprar")),
                new HashSet<>(Arrays.asList("Casas y pisos")), new HashSet<>(Arrays.asList("Barcelona")));
    }
}
