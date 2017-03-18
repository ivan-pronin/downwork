package com.idealista.scraper.model.search;

import com.idealista.scraper.model.filter.FilterAttributes;

public class GenericSearchFilterContext
{
    private SearchAttributes searchAttributes;

    private FilterAttributes filterAttributes;

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
}
