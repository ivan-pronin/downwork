package com.idealista.scraper.model;

import java.util.Comparator;

public class CategoryDescendingComparator implements Comparator<Category>
{
    @Override
    public int compare(Category o1, Category o2)
    {
        return -1 * new CategoryAscendingComparator().compare(o1, o2);
    }
}
