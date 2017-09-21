package com.idealista.scraper.model.filter;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import com.idealista.scraper.model.filter.FilterAttributes.PublicationDateFilter;

@Component
public class IdealistaFilterAttributesFactory implements IFilterAttributesFactory
{
    @Override
    public FilterAttributes create(List<Map<String, List<String>>> filterAttributesData)
    {
        FilterAttributes filterAttributes = new FilterAttributes();
        String publicationDateFilter = filterAttributesData.get(0).get("publicationDateFilter").get(0);
        if (StringUtils.isEmpty(publicationDateFilter))
        {
            filterAttributes.setPublicationDateFilter(PublicationDateFilter.NO_FILTER);
        }
        if (publicationDateFilter.contains("48Hours"))
        {
            filterAttributes.setPublicationDateFilter(PublicationDateFilter.LAST_48_HOURS);
        }
        if (publicationDateFilter.contains("24Hours"))
        {
            filterAttributes.setPublicationDateFilter(PublicationDateFilter.LAST_24_HOURS);
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
