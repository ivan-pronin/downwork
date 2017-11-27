package com.idealista.scraper.scraping.category.provider;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.search.CategoryChoosingAttribute;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.model.search.VibboSearchAttributes;
import com.idealista.scraper.scraping.category.chooser.ICategoriesChooser;
import com.idealista.scraper.scraping.category.chooser.VibboCategoriesChooser;
import com.idealista.scraper.util.CollectionUtils;

public class VibboCategoriesProvider extends AbstractCategoriesProvider implements ICategoriesProvider
{
    @Autowired
    private CollectionUtils collectionUtils;

    @Autowired
    private Supplier<ICategoriesChooser> categoriesChooserSupplier;

    @Override
    public Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext)
    {
        SearchAttributes searchAttributes = searchFilterContext.getSearchAttributes();
        Set<String> userTypologies = collectionUtils.wrapWithIdentificationFlag(searchAttributes.getTypologies(),
                VibboSearchAttributes.TYPOLOGY);
        Set<String> userOperations = collectionUtils.wrapWithIdentificationFlag(searchAttributes.getOperations(),
                VibboSearchAttributes.OPERATION);
        Set<String> userPropertyTypes = collectionUtils.wrapWithIdentificationFlag(searchAttributes.getPropertyTypes(),
                VibboSearchAttributes.PROPERTY_TYPE);
        Set<String> advertiser = collectionUtils.wrapWithIdentificationFlag(
                new HashSet<>(Arrays.asList(searchAttributes.getAdvertiser())), VibboSearchAttributes.ADVERTISER);
        Set<Set<String>> sets = new HashSet<>();
        sets.add(advertiser);
        sets.add(userPropertyTypes);
        sets.add(userOperations);
        sets.add(userTypologies);

        Set<CategoryChoosingAttribute> categoryAttributes = collectionUtils.getAllItemCombinations(sets);

        Queue<Callable<Set<Category>>> results = new ConcurrentLinkedQueue<>();
        categoryAttributes.forEach(categoryAttribute ->
        {
            VibboCategoriesChooser chooser = (VibboCategoriesChooser) categoriesChooserSupplier.get();
            chooser.setAttribute(categoryAttribute);
            results.add(chooser);
        });
        return executeAndGetResultsForSet(results);
    }
}
