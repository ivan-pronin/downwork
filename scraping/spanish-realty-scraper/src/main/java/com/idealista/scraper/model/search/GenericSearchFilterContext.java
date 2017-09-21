package com.idealista.scraper.model.search;

import java.util.List;
import java.util.Map;

import com.idealista.scraper.model.filter.FilterAttributes;

public class GenericSearchFilterContext
{
    private SearchAttributes searchAttributes;

    private FilterAttributes filterAttributes;

    private List<Map<String, List<String>>> genericFilterAttributes;

    private String province;

    public FilterAttributes getFilterAttributes()
    {
        return filterAttributes;
    }

    public String getProvince()
    {
        return province;
    }

    public SearchAttributes getSearchAttributes()
    {
        return searchAttributes;
    }

    public void setFilterAttributes(FilterAttributes filterAttributes)
    {
        this.filterAttributes = filterAttributes;
    }

    public void setProvince(String province)
    {
        this.province = province;
    }

    public void setSearchAttributes(SearchAttributes searchAttributes)
    {
        this.searchAttributes = searchAttributes;
    }

    public List<Map<String, List<String>>> getGenericFilterAttributes()
    {
        return genericFilterAttributes;
    }

    public void setGenericFilterAttributes(List<Map<String, List<String>>> genericFilterAttributes)
    {
        this.genericFilterAttributes = genericFilterAttributes;
    }
}
