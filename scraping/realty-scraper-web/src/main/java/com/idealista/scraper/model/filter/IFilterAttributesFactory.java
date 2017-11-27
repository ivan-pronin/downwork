package com.idealista.scraper.model.filter;

import java.util.List;
import java.util.Map;

public interface IFilterAttributesFactory
{
    FilterAttributes create(List<Map<String, List<String>>> filterAttributes);
}
