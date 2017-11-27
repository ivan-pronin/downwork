package com.idealista.scraper.scraping.category;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.CategoryDescendingComparator;
import com.idealista.web.config.BaseScraperConfiguration;

@Component
public class FoundUrlsManager implements IFoundUrlsManager
{
    @Autowired
    private BaseScraperConfiguration scraperConfiguration;

    @Override
    public Set<Category> getNewestAdsById(Set<Category> foundUls)
    {
        int newestAdsCount = scraperConfiguration.getNewestAdsCount();
        if (newestAdsCount <= 0)
        {
            return foundUls;
        }
        return foundUls.stream().sorted(new CategoryDescendingComparator()).limit(newestAdsCount)
                .collect(Collectors.toCollection(() -> new LinkedHashSet<>()));
    }
}
