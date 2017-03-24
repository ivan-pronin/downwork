package com.idealista.scraper.scraping.category;

import com.idealista.scraper.model.Category;
import com.idealista.scraper.model.CategoryDescendingComparator;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class FoundUrlsManager implements IFoundUrlsManager
{
    @Value("#{ '${newestAdsCount}'.isEmpty() ? '0' : '${newestAdsCount}' }")
    private int newestAdsCount;

    @Override
    public Set<Category> getNewestAdsById(Set<Category> foundUls)
    {
        if (newestAdsCount <= 0)
        {
            return foundUls;
        }
        return foundUls.stream().sorted(new CategoryDescendingComparator()).limit(newestAdsCount)
                .collect(Collectors.toCollection(() -> new LinkedHashSet<>()));
    }
}
