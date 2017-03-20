package com.codigospostales.scrap;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class PostalCodesListingPage
{
    @Autowired
    private WebDriver driver;

    public Set<PostalCodeCategory> getPostalCodesCategories()
    {
        Set<PostalCodeCategory> results = new HashSet<>();
        List<WebElement> fieldSets = driver.findElements(By.tagName("fieldset"));
        for (WebElement field : fieldSets)
        {
            PostalCodeCategory category = new PostalCodeCategory();
            WebElement legend = field.findElement(By.tagName("legend"));
            if (legend != null)
            {
                category.setProvince(legend.getText());
            }
            List<WebElement> postalCodes = field.findElements(By.xpath(".//li[@class='grande']//a"));
            Set<URL> urls = new HashSet<>();
            category.setPostCodeUrls(urls);
            postalCodes.forEach(e -> {
                try
                {
                    urls.add(new URL(e.getAttribute("href")));
                }
                catch (MalformedURLException e1)
                {
                    e1.printStackTrace();
                }
            });
            results.add(category);
        }
        return results;
    }
}
