package com.idealista.scraper.scraping.category.provider;

import java.util.List;
import java.util.Map;
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
import com.idealista.scraper.scraping.category.chooser.PisosCategoriesChooser;
import com.idealista.scraper.ui.page.IStartPage;
import com.idealista.scraper.ui.page.pisos.PisosStartPage;

public class PisosCategoriesProvider extends AbstractCategoriesProvider implements ICategoriesProvider
{
    @Autowired
    private Supplier<ICategoriesChooser> categoriesChooserSupplier;

    @Autowired
    private Supplier<IStartPage> startPageSupplier;

    @Override
    public Set<Category> getCategoriesUrls(GenericSearchFilterContext searchFilterContext)
    {
        SearchAttributes searchAttributes = searchFilterContext.getSearchAttributes();
        List<Map<String, List<String>>> filterAttributes = searchFilterContext.getGenericFilterAttributes();

        Set<String> userOperations = searchAttributes.getOperations();
        Set<String> userTypologies = searchAttributes.getTypologies();
        Set<String> userLocations = searchAttributes.getLocations();
        PisosStartPage startPage = (PisosStartPage) startPageSupplier.get();

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
                            for (Map<String, List<String>> filterSet : filterAttributes)
                            {
                                for (String district : filterSet.get("distro"))
                                {
                                    PisosCategoriesChooser chooser = (PisosCategoriesChooser) categoriesChooserSupplier
                                            .get();
                                    chooser.setOperation(operation);
                                    chooser.setTypology(typology);
                                    chooser.setLocation(location);
                                    chooser.setZone(filterSet.get("zone").get(0));
                                    chooser.setMunicipio(filterSet.get("municipio").get(0));
                                    chooser.setDistrict(district);
                                    chooser.setExtras(filterSet.get("extras").get(0));
                                    results.add(chooser);
                                }
                            }
                        }
                    }
                }
            }
        }
        return executeAndGetResultsForSet(results);
    }
}
