package com.idealista.scraper.scraping.category.chooser;

import java.util.Set;
import java.util.concurrent.Callable;

import com.idealista.scraper.model.Category;

public interface ICategoriesChooser extends Callable<Set<Category>>
{

}
