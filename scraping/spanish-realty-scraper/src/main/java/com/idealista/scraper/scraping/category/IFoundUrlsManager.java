package com.idealista.scraper.scraping.category;

import com.idealista.scraper.model.Category;

import java.util.Set;

public interface IFoundUrlsManager
{
    Set<Category> getNewestAdsById(Set<Category> foundUls);
}
