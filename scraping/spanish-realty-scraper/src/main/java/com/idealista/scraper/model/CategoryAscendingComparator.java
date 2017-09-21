package com.idealista.scraper.model;

import java.util.Comparator;

import com.idealista.scraper.util.URLUtils;

public class CategoryAscendingComparator implements Comparator<Category>
{
    @Override
    public int compare(Category o1, Category o2)
    {
        long id = URLUtils.extractIdFromUrl(o1.getUrl());
        long otherId = URLUtils.extractIdFromUrl(o2.getUrl());
        return Long.compare(id, otherId);
    }
}
