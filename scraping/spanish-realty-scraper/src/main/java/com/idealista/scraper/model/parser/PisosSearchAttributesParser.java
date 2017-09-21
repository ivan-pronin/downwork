package com.idealista.scraper.model.parser;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.PisosSearchAttributes;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.ui.page.pisos.PisosStartPage;

@Component
public class PisosSearchAttributesParser implements ISearchAttributesParser
{

    @Override
    public SearchAttributes parseSearchAttributes(Map<IGenericSearchAttributes, Set<String>> attributes)
    {
        Set<String> operations = attributes.get(PisosSearchAttributes.OPERATION);
        Set<String> typologies = attributes.get(PisosSearchAttributes.TYPOLOGY);
        Set<String> locations = attributes.get(PisosSearchAttributes.LOCATION);
        PisosStartPage startPage = new PisosStartPage();
        if (operations.isEmpty())
        {
            operations = startPage.getAvailableOperations();
        }
        if (typologies.isEmpty())
        {
            typologies = startPage.getAvailableTypologies();
        }
        if (locations.isEmpty())
        {
            locations = startPage.getAvailableLocations();
        }
        return new SearchAttributes(operations, typologies, locations);
    }
}
