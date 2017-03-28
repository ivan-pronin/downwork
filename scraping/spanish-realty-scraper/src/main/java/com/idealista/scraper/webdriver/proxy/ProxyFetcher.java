package com.idealista.scraper.webdriver.proxy;

import com.idealista.scraper.ui.ClickActions;
import com.idealista.scraper.ui.SearchActions;
import com.idealista.scraper.util.FileUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Component
public class ProxyFetcher
{
    private static final String US_PROXY_ORG = "https://www.us-proxy.org/";
    private static final String FREE_PROXY_CZ = "http://free-proxy.cz/en/proxylist/main/date/1";
    private static final Logger LOGGER = LogManager.getLogger(ProxyFetcher.class);

    private WebDriver driver;

    @Autowired
    private SearchActions searchActions;

    @Autowired
    private ClickActions clickActions;

    @Value("#{ T(java.util.Arrays).asList(${proxySources}) }")
    private List<Integer> proxySources;

    public Set<String> fetchProxies()
    {
        Set<String> proxies = new HashSet<>();
        if (proxySources.contains(1))
        {
            proxies.addAll(fetchProxiesFromUsProxyOrg());
        }
        if (proxySources.contains(2))
        {
            proxies.addAll(fetchProxiesFromFreeProxyCz());
        }
        LOGGER.info("Printing all fetched proxies ... ");
        proxies.forEach(LOGGER::info);
        driver.quit();
        return proxies;
    }

    public void setDriver(WebDriver driver)
    {
        this.driver = driver;
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
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

    private Set<String> fetchProxiesFromFreeProxyCz()
    {
        LOGGER.info("Fetching new proxies from {}", FREE_PROXY_CZ);
        driver.get(FREE_PROXY_CZ);
        Set<String> proxies = new HashSet<>();
        for (int i = 0; i < 9; i++)
        {
            proxies.addAll(getProxiesFromNextFreeProxyCzPage(driver));
        }
        LOGGER.info("Fetched <{}> proxies", proxies.size());
        return proxies;
    }

    private Set<String> fetchProxiesFromUsProxyOrg()
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
                    List<WebElement> table = searchActions.findElementsById("proxylisttable");
                    if (!table.isEmpty())
                    {
                        List<WebElement> rows = searchActions.findElementsByXpath(table, "//tbody//tr");
                        proxies = rows.stream().map(this::extractProxyFromRow).collect(Collectors.toSet());
                        LOGGER.info("Fetched <{}> proxies", proxies.size());
                    }
                }
            }
        }
        return proxies;
    }

    private Set<String> getProxiesFromNextFreeProxyCzPage(WebDriver driver)
    {
        Set<String> proxies = new HashSet<>();
        List<WebElement> exportButton = searchActions.findElementsById("clickexport");
        clickActions.click(exportButton);
        WebElement proxiesBlock = searchActions.waitForElement(By.id("zkzk"), 5);
        if (proxiesBlock != null)
        {
            proxies.addAll(FileUtils.readStringToLines(proxiesBlock.getText()));
        }
        clickActions.click(exportButton);
        List<WebElement> nextButton = searchActions.findElementsByXpath("//a[contains(.,'Next')]");
        clickActions.click(nextButton);
        return proxies;
    }
}
