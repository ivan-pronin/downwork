package com.idealista.scraper.scraping.paginator;

import com.idealista.scraper.model.Category;

import java.util.Set;

public interface IPaginator
{
    Set<Category> getAllPageUrls(Category baseCategory);
}
