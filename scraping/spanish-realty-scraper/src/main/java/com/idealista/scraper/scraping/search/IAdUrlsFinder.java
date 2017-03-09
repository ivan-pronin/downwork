package com.idealista.scraper.scraping.search;

import com.idealista.scraper.model.Category;

import java.util.Set;

public interface IAdUrlsFinder
{
    Set<Category> findNewAdUrlsAmount(int neededAmount);
}
