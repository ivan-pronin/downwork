package com.idealista.scraper.scraping.category;

import java.util.Set;

import com.idealista.scraper.model.Category;

public interface IFoundUrlsManager
{
    Set<Category> getNewestAdsById(Set<Category> foundUls);
}
