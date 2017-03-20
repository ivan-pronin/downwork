package com.codigospostales.scrap;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class PostalCodesService
{
    private static final String POST_CODES_URL = "https://www.codigospostales.com/nestcp.cgi?28";

    @Autowired
    private WebDriver driver;

    @Autowired
    private PostalCodesListingPage postalCodesListingPage;

    @Autowired
    private XlsExporter xlsExporter;

    public void scrapPostalCodes()
    {
        driver.get(POST_CODES_URL);
        Set<PostalCodeCategory> categories = postalCodesListingPage.getPostalCodesCategories();

        Set<PostalCode> postalCodes = new HashSet<>();

        for (PostalCodeCategory category : categories)
        {
            for (URL url : category.getPostCodeUrls())
            {
                driver.get(url.toString());
                PostalCode code = new PostalCode();
                WebElement number = driver.findElement(By.tagName("legend"));
                code.setNumber(number.getText());
                List<WebElement> streets = driver.findElements(By.xpath("//li[@class='lateral']//a"));
                code.setStreets(streets.stream().map(e -> e.getText()).collect(Collectors.toSet()));
                code.setProvince(category.getProvince());
                postalCodes.add(code);
                System.out.println("Found: " + code);
            }
        }
        System.out.println("Total codes processed: " + postalCodes.size());

        xlsExporter.writeResultsToXls(postalCodes);
    }
}
