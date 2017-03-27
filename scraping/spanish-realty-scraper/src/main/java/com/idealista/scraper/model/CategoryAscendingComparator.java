package com.idealista.scraper.model;

import com.idealista.scraper.util.RegexUtils;

import java.util.Comparator;

public class CategoryAscendingComparator implements Comparator<Category>
{
    @Override
    public int compare(Category o1, Category o2)
    {
        int id = Integer.parseInt(RegexUtils.extractPostalCode(o1.getUrl().toString()));
        int otherId = Integer.parseInt(RegexUtils.extractPostalCode(o2.getUrl().toString()));
        return id - otherId;
    }
}
