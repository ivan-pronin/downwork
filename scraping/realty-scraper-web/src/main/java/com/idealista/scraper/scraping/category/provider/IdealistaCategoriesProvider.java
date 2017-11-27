package com.idealista.scraper.scraping.category.provider;

import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.function.Supplier;

import org.springframework.beans.factory.annotation.Autowired;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.search.GenericSearchFilterContext;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.scraping.category.chooser.ICategoriesChooser;
import com.idealista.scraper.scraping.category.chooser.IdealistaCategoriesChooser;
import com.idealista.scraper.ui.page.IStartPage;
import com.idealista.scraper.ui.page.idealista.IdealistaStartPage;

public class IdealistaCategoriesProvider extends AbstractCategoriesProvider implements ICategoriesProvider
{
    @Autowired
    private Supplier<ICategoriesChooser> categoriesChooserSupplier;

    @Autowired
    private Supplier<IStartPage> startPageSupplier;

    @Override
    public Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext)
    {
        SearchAttributes searchAttributes = searchFilterContext.getSearchAttributes();
        Set<String> userOperations = searchAttributes.getOperations();
        Set<String> userTypologies = searchAttributes.getTypologies();
        Set<String> userLocations = searchAttributes.getLocations();
        IdealistaStartPage startPage = (IdealistaStartPage) startPageSupplier.get();

        Queue<Callable<Set<Category>>> results = new ConcurrentLinkedQueue<>();

        for (String operation : userOperations)
        {
            startPage.selectOperation(operation);
            Set<String> availableTypologies = startPage.getAvailableTypologies();

            for (String typology : availableTypologies)
            {
                if (userTypologies.contains(typology))
                {
                    startPage.selectTypology(typology);
                    Set<String> availableLocations = startPage.getAvailableLocations();
                    for (String location : availableLocations)
                    {
                        if (userLocations.contains(location))
                        {
                            startPage.selectLocation(location);
                            IdealistaCategoriesChooser iCategoriesChooser = (IdealistaCategoriesChooser) categoriesChooserSupplier
                                    .get();
                            iCategoriesChooser.setOperation(operation);
                            iCategoriesChooser.setLocation(location);
                            iCategoriesChooser.setTypology(typology);
                            iCategoriesChooser.setProvince(searchFilterContext.getProvince());
                            iCategoriesChooser.setFilterAttributes(searchFilterContext.getFilterAttributes());
                            results.add(iCategoriesChooser);
                        }
                    }
                }
            }
        }
        return executeAndGetResultsForSet(results);
    }
}
