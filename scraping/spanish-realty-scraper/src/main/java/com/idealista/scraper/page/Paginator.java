package com.idealista.scraper.page;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.IntStream;

public class Paginator
{
    private static final Logger LOGGER = LogManager.getLogger(Paginator.class);

    public Set<URL> getAllPageUrls(WebDriver driver, String basePage)
    {
        driver.navigate().to(basePage);
        List<WebElement> pagination = driver.findElements(By.xpath("//div[@class='pagination']"));
        if (pagination.isEmpty())
        {
            LOGGER.warn("Failed to find pagination for category page: {}", basePage);
            try
            {
                return Collections.singleton(new URL(basePage));
            }
            catch (MalformedURLException e1)
            {
                e1.printStackTrace();
                return Collections.emptySet();
            }
        }
        List<WebElement> pages = pagination.get(0).findElements(By.xpath(".//li"));
        if (pages.isEmpty())
        {
            LOGGER.error("Failed to find pagination links");
            return Collections.emptySet();
        }
        int totalPages = Integer.parseInt(pages.get(pages.size() - 2).getText());
        LOGGER.info("Total pages: " + totalPages);
        String basePagePath = driver.getCurrentUrl();
        Set<URL> pagesToProcess = new HashSet<>();
        IntStream.range(1, totalPages + 1).forEach(e -> pagesToProcess.add((generateUrl(basePagePath, e))));
        LOGGER.info("Pages to process counT: " + pagesToProcess.size());
        return pagesToProcess;
    }

    private URL generateUrl(String basePagePath, int pageNumber)
    {
        try
        {
            return new URL(basePagePath + "pagina-" + pageNumber + ".htm");
        }
        catch (MalformedURLException e)
        {
            LOGGER.error("Error while creating URL from string: {}, error: {}", basePagePath, e);
        }
        return null;
    }
}
