package com.idealista.scraper.model.parser;

import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.SearchAttributes;
import com.idealista.scraper.model.search.VibboSearchAttributes;

import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Set;

@Component
public class VibboSearchAttributesParser implements ISearchAttributesParser
{
    @Override
    public SearchAttributes parseSearchAttributes(Map<IGenericSearchAttributes, Set<String>> attributes)
    {
        Set<String> typologies = attributes.get(VibboSearchAttributes.TYPOLOGY);
        Set<String> operations = attributes.get(VibboSearchAttributes.OPERATION);
        Set<String> propertyTypes = attributes.get(VibboSearchAttributes.PROPERTY_TYPE);
        Set<String> advertiser = attributes.get(VibboSearchAttributes.ADVERTISER);
        SearchAttributes searchAttriburtes = new SearchAttributes();
        searchAttriburtes.setTypologies(typologies);
        searchAttriburtes.setOperations(operations);
        searchAttriburtes.setPropertyTypes(propertyTypes);
        searchAttriburtes.setAdvertiser(advertiser.isEmpty() ? "" : advertiser.iterator().next());
        return searchAttriburtes;
    }
}
