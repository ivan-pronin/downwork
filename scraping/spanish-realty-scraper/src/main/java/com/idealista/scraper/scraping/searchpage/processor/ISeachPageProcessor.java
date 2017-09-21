package com.idealista.scraper.scraping.searchpage.processor;

import java.util.Set;
import java.util.concurrent.Callable;

import com.idealista.scraper.model.Category;

public interface ISeachPageProcessor extends Callable<Set<Category>>
{
    void setPage(Category page);
}
