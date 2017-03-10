package com.idealista.scraper.webdriver.proxy;

import com.idealista.scraper.ui.SearchActions;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProxyFetcher
{
    private static final String US_PROXY_ORG = "https://www.us-proxy.org/";
    private static final Logger LOGGER = LogManager.getLogger(ProxyFetcher.class);

    private WebDriver driver;

    @Autowired
    private SearchActions searchActions;

    public Set<String> fetchProxies()
    {
        LOGGER.info("Fetching new proxies from {}", US_PROXY_ORG);
        driver.get(US_PROXY_ORG);
        List<WebElement> select = searchActions.findElementsByXpath("//select[@name='proxylisttable_length']");
        Set<String> proxies = new HashSet<>();
        if (!select.isEmpty())
        {
            List<WebElement> options = searchActions.findElementsByXpath(select, "//option");
            for (WebElement option : options)
            {
                if (option.getAttribute("value").equalsIgnoreCase("80"))
                {
                    option.click();
                    List<WebElement> table = searchActions.findElementsById(Collections.emptyList(), "proxylisttable");
                    if (!table.isEmpty())
                    {
                        List<WebElement> rows = searchActions.findElementsByXpath(table, "//tbody//tr");
                        proxies = rows.stream().map(this::extractProxyFromRow).collect(Collectors.toSet());
                        LOGGER.info("Fetched <{}> proxies, printing them...", proxies.size());
                        proxies.forEach(LOGGER::info);
                    }
                }
            }
        }
        driver.quit();
        return proxies;
    }

    private String extractProxyFromRow(WebElement row)
    {
        List<WebElement> tds = searchActions.findElementsByTagName(Arrays.asList(row), "td");
        if (tds.size() >= 2)
        {
            return tds.get(0).getText() + ':' + tds.get(1).getText();
        }
        return null;
    }

    public void setDriver(WebDriver driver)
    {
        this.driver = driver;
        searchActions.setWebDriver(driver);
    }
}
