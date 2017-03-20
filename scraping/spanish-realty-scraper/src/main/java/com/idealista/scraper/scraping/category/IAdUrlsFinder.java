package com.idealista.scraper.scraping.category;

import com.idealista.scraper.model.Category;

import java.util.Set;

public interface IAdUrlsFinder
{
    Set<Category> findNewAdUrlsAmount(int neededAmount);
}
