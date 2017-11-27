package com.idealista.scraper.scraping.category.provider;

import java.util.HashSet;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.filter.FilterAttributes;
import com.idealista.scraper.model.search.CategoryChoosingAttribute;
import com.idealista.scraper.model.search.FotocasaSearchAttributes;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.scraping.category.chooser.FotocasaCategoriesChooser;
import com.idealista.scraper.scraping.category.chooser.ICategoriesChooser;
import com.idealista.scraper.util.CollectionUtils;

public class FotocasaCategoriesProvider extends AbstractCategoriesProvider implements ICategoriesProvider
{
    @Autowired
    private Supplier<ICategoriesChooser> categoriesChooserSupplier;

    @Autowired
    private CollectionUtils collectionUtils;

    @Override
    public Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext)
    {
        SearchAttributes searchAttributes = searchFilterContext.getSearchAttributes();
        FilterAttributes filterAttributes = searchFilterContext.getFilterAttributes();

        Set<String> userOperations = collectionUtils.wrapWithIdentificationFlag(searchAttributes.getOperations(),
                FotocasaSearchAttributes.OPERATION);
        Set<String> userDistricts = collectionUtils.wrapWithIdentificationFlag(filterAttributes.getDisctricts(),
                FotocasaSearchAttributes.DISTRICT);
        Set<Set<String>> sets = new HashSet<>();
        sets.add(userOperations);
        sets.add(userDistricts);

        Set<CategoryChoosingAttribute> categoryAttributes = collectionUtils.getAllItemCombinations(sets);
        categoryAttributes.forEach(e ->
        {
            e.setNewHomes(filterAttributes.isNewHomes());
            e.setSearchString(searchAttributes.getSearchString());
        });

        Queue<Callable<Set<Category>>> tasks = new ConcurrentLinkedQueue<>();
        for (CategoryChoosingAttribute attribute : categoryAttributes)
        {
            FotocasaCategoriesChooser fotocasaCategoriesChooser = (FotocasaCategoriesChooser) categoriesChooserSupplier
                    .get();
            fotocasaCategoriesChooser.setAttribute(attribute);
            tasks.add(fotocasaCategoriesChooser);
        }
        return executeAndGetResultsForSet(tasks);
    }
}
