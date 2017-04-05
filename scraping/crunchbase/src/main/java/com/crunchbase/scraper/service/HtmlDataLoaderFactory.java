package com.crunchbase.scraper.service;

import com.crunchbase.scraper.model.Company;
import com.crunchbase.scraper.webdriver.INavigateActions;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class HtmlDataLoaderFactory
{
    @Value("${targetFile}")
    private String targetFile;

    @Autowired
    private INavigateActions navigateActions;
    
    public HtmlDataLoader create(Company inputCompany)
    {
        HtmlDataLoader htmlDataLoader = new HtmlDataLoader();
        htmlDataLoader.setNavigateActions(navigateActions);
        htmlDataLoader.setInputCompany(inputCompany);
        htmlDataLoader.setTargetFile(targetFile);
        return htmlDataLoader;
    }
}
