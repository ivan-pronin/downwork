package com.idealista.scraper.scraping.searchpage.processor;

import com.idealista.scraper.model.Category;

import java.util.Set;
import java.util.concurrent.Callable;

public interface ISeachPageProcessor extends Callable<Set<Category>>
{
    
}
