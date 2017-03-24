package com.idealista.scraper.model;

import com.idealista.scraper.util.RegexUtils;

import java.util.Comparator;

public class CategoryAscendingComparator implements Comparator<Category>
{
    @Override
    public int compare(Category o1, Category o2)
    {
        int id = RegexUtils.extractBigNumber(o1.getUrl().toString());
        int otherId = RegexUtils.extractBigNumber(o2.getUrl().toString());
        return id - otherId;
    }
}
