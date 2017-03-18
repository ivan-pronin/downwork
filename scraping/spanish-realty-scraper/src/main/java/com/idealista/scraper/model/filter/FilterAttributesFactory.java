package com.idealista.scraper.model.filter;

import com.idealista.scraper.model.filter.FilterAttributes.PublicationDateFilter;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class FilterAttributesFactory implements IFilterAttributesFactory
{
    @Override
    public FilterAttributes create(String publicationDateFilter)
    {
        FilterAttributes filterAttributes = new FilterAttributes();
        if (StringUtils.isEmpty(publicationDateFilter))
        {
            filterAttributes.setPublicationDateFilter(PublicationDateFilter.NO_FILTER);
        }
        if (publicationDateFilter.contains("48Hours"))
        {
            filterAttributes.setPublicationDateFilter(PublicationDateFilter.LAST_48_HOURS);
        }
        if (publicationDateFilter.contains("lastWeek"))
        {
            filterAttributes.setPublicationDateFilter(PublicationDateFilter.LAST_WEEK);
        }
        if (publicationDateFilter.contains("lastMonth"))
        {
            filterAttributes.setPublicationDateFilter(PublicationDateFilter.LAST_MONTH);
        }
        return filterAttributes;
    }
}
