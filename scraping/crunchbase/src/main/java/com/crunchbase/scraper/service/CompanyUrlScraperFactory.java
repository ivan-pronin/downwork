package com.crunchbase.scraper.service;

import com.crunchbase.scraper.model.Company;
import com.crunchbase.scraper.ui.PageLoader;
import com.crunchbase.scraper.webdriver.WebDriverProvider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class CompanyUrlScraperFactory
{
    @Autowired
    private WebDriverProvider webDriverProvider;
    
    @Autowired
    private PageLoader pageLoader;
    
    public CompanyUrlScraper create(Company inputCompany)
    {
        CompanyUrlScraper companyUrlScraper = new CompanyUrlScraper();
        companyUrlScraper.setWebDriverProvider(webDriverProvider);
        companyUrlScraper.setInputCompany(inputCompany);
        companyUrlScraper.setPageLoader(pageLoader);
        return companyUrlScraper;
    }
}
