package com.idealista.scraper.service;

import com.idealista.scraper.search.Category;

import java.util.Set;

public interface IAdUrlsFinder
{
    Set<Category> findNewAdUrlsAmount(int neededAmount);
}
