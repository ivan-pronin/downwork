package com.idealista.scraper.scraping.paginator;

import java.util.Set;

import com.idealista.scraper.model.Category;

public interface IPaginator
{
    Set<Category> getAllPageUrls(Category baseCategory);
}
