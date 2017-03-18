package com.idealista.scraper.model.parser;

import com.idealista.scraper.model.search.IGenericSearchAttributes;
import com.idealista.scraper.model.search.SearchAttributes;

import java.util.Map;
import java.util.Set;

public interface ISearchAttributesParser
{
    SearchAttributes parseSearchAttributes(Map<IGenericSearchAttributes, Set<String>> attributes);
}
