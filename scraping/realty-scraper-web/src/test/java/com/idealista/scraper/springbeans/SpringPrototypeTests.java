package com.idealista.scraper.springbeans;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.scraping.category.chooser.FotocasaSubDistrictChooser;
import com.idealista.scraper.scraping.category.provider.ICategoriesProvider;

@ContextConfiguration(classes = AppConfig.class)
@RunWith(SpringJUnit4ClassRunner.class)
public class SpringPrototypeTests
{
    @Value("${operation}")
    private String userOperation;

    @Value("${searchString}")
    private String userSearchString;

    @Value("${newHomes}")
    private boolean newHomes;

    @Value("#{ T(org.springframework.util.StringUtils).commaDelimitedListToSet('${districts}') }")
    private Set<String> userDistricts;

    @Autowired
    private ICategoriesProvider categoriesProvider;

    @Autowired
    private Supplier<FotocasaSubDistrictChooser> fotocasaSubDistrictChooser;

    @Test
    public void testFotocasaSubdistrictsDependencies() throws Exception
    {
        FotocasaSubDistrictChooser schooser1 = fotocasaSubDistrictChooser.get();
        FotocasaSubDistrictChooser schooser2 = fotocasaSubDistrictChooser.get();
        System.out.println(schooser1);
        System.out.println("...........................");
        System.out.println("...........................");
        System.out.println(schooser2);
        Assert.assertFalse(schooser1 == schooser2);
    }

    @Test
    public void testFotocasaChooser() throws Exception
    {
        SearchAttributes searchAttributes = new SearchAttributes();
        searchAttributes.setOperations(new HashSet<>(Arrays.asList(userOperation)));
        searchAttributes.setSearchString(userSearchString);
        FilterAttributes filterAttributes = new FilterAttributes();
        filterAttributes.setNewHomes(newHomes);
        filterAttributes.setDisctricts(userDistricts);
        GenericSearchFilterContext context = new GenericSearchFilterContext();
        context.setSearchAttributes(searchAttributes);
        context.setFilterAttributes(filterAttributes);

        Set<Category> results = categoriesProvider.getCategoriesUrls(context);
        System.out.println(results.size());
        results.forEach(System.out::println);
        Assert.assertEquals(20, results.size());
    }

}
