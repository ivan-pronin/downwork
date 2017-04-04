package com.crunchbase.scraper.service;

import com.crunchbase.scraper.executor.ExecutorServiceProvider;
import com.crunchbase.scraper.model.Company;
import com.crunchbase.scraper.util.CsvUtils;
import com.crunchbase.scraper.util.FileUtils;
import com.crunchbase.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Component
public class CrunchbaseScraperService
{
    public static final String CRUNCHBASE_COM = "https://www.crunchbase.com/#/home/index";

    private static final Logger LOGGER = LogManager.getLogger(CrunchbaseScraperService.class);

    @Value("${srcFile}")
    private String srcFile;

    @Value("${targetFile}")
    private String targetFile;

    @Value("${readStartRow}")
    private int readStartRow;

    @Value("${readEndRow}")
    private int readEndRow;

    @Autowired
    private WebDriverProvider webDriverProvider;
    
    @Autowired
    private CompanyUrlScraperFactory companyUrlScraperFactory; 
    
    @Autowired
    private HtmlDataLoaderFactory htmlDataLoaderFactory; 
    
    @Autowired
    private ExecutorServiceProvider executor;

    public void scrap()
    {
        Set<Company> companies = Collections.synchronizedSet(new LinkedHashSet<>());
        Set<String> companyTitles = CsvUtils.readCsvToString(srcFile, readStartRow, readEndRow);
//        Set<String> companyTitles = new HashSet<>(Arrays.asList("1Apeiron"));
//        Set<String> companyTitles = FileUtils.readFileToLines("firms.txt");
        
        companyTitles.forEach(System.out::println);
        
        companyTitles.forEach(e -> companies.add(new Company(e)));

        LOGGER.info("Companies size: {}", companies.size());
        
        Queue<Callable<Company>> companyUrlsTasks = new ConcurrentLinkedQueue<>();
        
        for (Company company : companies)
        {
            companyUrlsTasks.add(companyUrlScraperFactory.create(company));
        }
        
        List<Future<Company>> taskResults = new ArrayList<>();
        try
        {
            taskResults = executor.getExecutor().invokeAll(companyUrlsTasks);
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }

        companies.clear();
        taskResults.forEach(e -> {
            try
            {
                companies.add(e.get());
            }
            catch (InterruptedException | ExecutionException e1)
            {
                e1.printStackTrace();
            }
        });
        
        Queue<Callable<Company>> companyHtmlDataTasks = new ConcurrentLinkedQueue<>();
        
        for (Company company : companies)
        {
            companyHtmlDataTasks.add(htmlDataLoaderFactory.create(company));
        }
        
        taskResults.clear();
        try
        {
            taskResults = executor.getExecutor().invokeAll(companyHtmlDataTasks);
        }
        catch (InterruptedException e1)
        {
            e1.printStackTrace();
        }
        
        companies.clear();
        taskResults.forEach(e -> {
            try
            {
                companies.add(e.get());
            }
            catch (InterruptedException | ExecutionException e1)
            {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }
        });
        
        webDriverProvider.destroy();
        CsvUtils.updateInputFile(companies, srcFile, targetFile);
        executor.getExecutor().shutdown();
    }
}
