package com.crunchbase.scraper;

import com.crunchbase.scraper.model.Company;
import com.crunchbase.scraper.model.HtmlData;
import com.crunchbase.scraper.util.CsvUtils;

import org.junit.Test;

import java.net.URLEncoder;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Random;
import java.util.Set;

public class Tests
{
    
    @Test
    public void testName2() throws Exception
    {
        System.out.println(URLEncoder.encode("qOddENxeLxL+13drGKYUgA==\n", "UTF8"));
        System.out.println(URLEncoder.encode("qOddENxeLxL%2B13drGKYUgA%3D%3D%0A", "UTF8"));
    }
    //@Test
    public void testName() throws Exception
    {
        Set<Company> companies = new LinkedHashSet<>();
        Set<String> companyTitles = CsvUtils.readCsvToString("i2.csv");
        companyTitles.forEach(e -> companies.add(createCompany(e)));

        CsvUtils.updateInputFile(companies, "i2.csv", "new_i3.csv");
    }

    private Company createCompany(String title)
    {
        Company c = new Company(title);
        Set<HtmlData> data = createData();
        c.setHtmlData(data);
        return c;
    }

    private Set<HtmlData> createData()
    {
        Set<HtmlData> result = new HashSet<>();
        for (int i = 0; i < new Random().nextInt(5) ; i ++)
        {
            HtmlData item = new HtmlData();
            item.setFileName("fileName_" + i);
            result.add(item);
        }
        return result;
    }
}
