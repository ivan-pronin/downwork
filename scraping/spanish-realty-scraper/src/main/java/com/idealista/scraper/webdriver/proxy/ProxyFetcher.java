package com.idealista.scraper.webdriver.proxy;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import com.idealista.scraper.ui.actions.ClickActions;
import com.idealista.scraper.ui.actions.SearchActions;
import com.idealista.scraper.ui.actions.WaitActions;
import com.idealista.scraper.util.FileUtils;
import com.idealista.scraper.util.WebDriverUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProxyFetcher
{
    private static final String US_PROXY_ORG = "https://www.us-proxy.org/";
    private static final String FREE_PROXY_CZ = "http://free-proxy.cz/en/proxylist/main/date/1";
    private static final List<String> PROXIES_URLS = Arrays.asList("http://free-proxy-list.net/",
            "http://www.sslproxies.org/", "http://www.us-proxy.org/", "http://free-proxy-list.net/uk-proxy.html",
            "http://www.socks-proxy.net/", "http://free-proxy-list.net/anonymous-proxy.html");
    private static final String US_PROXY_PAGINATION_LINKS_LOCATOR = "//ul[@class='pagination']//li[not(@id)]//a";

    private static final Logger LOGGER = LogManager.getLogger(ProxyFetcher.class);

    private WebDriver driver;

    @Autowired
    private SearchActions searchActions;
    
    @Autowired
    private WaitActions waitActions;

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
        LOGGER.info("Total found proxies count: {}", proxies.size());
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
        LOGGER.info("Fetched <{}> proxies from FreeProxyCz", proxies.size());
        return proxies;
    }

    private Set<String> fetchProxiesFromUsProxyOrg()
    {
        LOGGER.info("Fetching new proxies from {}", US_PROXY_ORG);
        Set<String> proxies = new HashSet<>();
        for (String url : PROXIES_URLS)
        {
            proxies.addAll(getProxiesFromUrl(url));
        }
        LOGGER.info("Fetched <{}> proxies from UsProxyOrg", proxies.size());
        return proxies;
    }

    private Set<String> getProxiesFromUrl(String url)
    {
        driver.get(url);
        Set<String> proxies = new HashSet<>();
        List<WebElement> select = searchActions.findElementsByXpath("//select[@name='proxylisttable_length']");
        if (!select.isEmpty())
        {
            List<WebElement> options = searchActions.findElementsByXpath(select, "//option");
            for (WebElement option : options)
            {
                if (option.getAttribute("value").equalsIgnoreCase("80"))
                {
                    option.click();
                }
            }
        }
        List<WebElement> paginationLinks = searchActions.findElementsByXpath(US_PROXY_PAGINATION_LINKS_LOCATOR);
        for (int i = 0; i < paginationLinks.size(); i++)
        {
            if (i > 0)
            {
                WebElement link = searchActions.findElementsByXpath(US_PROXY_PAGINATION_LINKS_LOCATOR).get(i);
                clickActions.click(link);
            }
            WebDriverUtils.waitForAllContentToLoad(driver);
            List<WebElement> table = searchActions.findElementsById("proxylisttable");
            if (!table.isEmpty())
            {
                List<WebElement> rows = searchActions.findElementsByXpath(table, "//tbody//tr");
                proxies.addAll(rows.stream().map(this::extractProxyFromRow).collect(Collectors.toSet()));
            }
        }
        LOGGER.info("Found <{}> proxies from URL: {}", proxies.size(), url);
        return proxies;
    }

    private Set<String> getProxiesFromNextFreeProxyCzPage(WebDriver driver)
    {
        Set<String> proxies = new HashSet<>();
        List<WebElement> exportButton = searchActions.findElementsById("clickexport");
        clickActions.click(exportButton);
        WebElement proxiesBlock = waitActions.waitForElement(By.id("zkzk"), 5);
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
