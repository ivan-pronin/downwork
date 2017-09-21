package com.idealista.scraper.model.parser;

import java.util.Map;
import java.util.Set;

import org.springframework.stereotype.Component;

import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.IdealistaSearchAttributes;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.ui.page.idealista.IdealistaStartPage;

@Component
public class IdealistaSearchAttributesParser implements ISearchAttributesParser
{
    private IdealistaStartPage startPage;

    @Override
    public SearchAttributes parseSearchAttributes(Map<IGenericSearchAttributes, Set<String>> attributes)
    {
        Set<String> operations = attributes.get(IdealistaSearchAttributes.OPERATION);
        Set<String> typologies = attributes.get(IdealistaSearchAttributes.TYPOLOGY);
        Set<String> locations = attributes.get(IdealistaSearchAttributes.LOCATION);
        startPage = new IdealistaStartPage();
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
