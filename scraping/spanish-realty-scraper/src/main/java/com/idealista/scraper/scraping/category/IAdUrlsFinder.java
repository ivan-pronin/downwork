package com.idealista.scraper.scraping.category;

import java.util.Set;

import com.idealista.scraper.model.Category;

public interface IAdUrlsFinder
{
    Set<Category> findNewAdUrlsAmount(int neededAmount);
}
