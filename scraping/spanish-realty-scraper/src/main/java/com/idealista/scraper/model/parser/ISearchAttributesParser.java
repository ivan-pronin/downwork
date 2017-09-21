package com.idealista.scraper.model.parser;

import java.util.Map;
import java.util.Set;

import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.SearchAttributes;

public interface ISearchAttributesParser
{
    SearchAttributes parseSearchAttributes(Map<IGenericSearchAttributes, Set<String>> attributes);
}
