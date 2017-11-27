package com.idealista.scraper.webdriver.proxy;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.idealista.scraper.AppConfig;
import com.idealista.scraper.data.IDataSource;
import com.idealista.scraper.service.ScrapTarget;
import com.idealista.scraper.util.FileUtils;
import com.idealista.scraper.util.WaitUtils;
import com.idealista.scraper.webdriver.WebDriverFactory;
import com.idealista.scraper.webdriver.WebDriverFactory.DriverType;
import com.idealista.web.config.BaseScraperConfiguration;

@Component
public class ProxyProvider implements IProxyProvider
{
    private static final Logger LOGGER = LogManager.getLogger(ProxyProvider.class);

    public static final Pattern VALID_PROXY_ADDRESS_PATTERN = Pattern
            .compile("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
                    + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b:\\d{2,5}");

    private Set<String> proxiesInputData = new HashSet<>();
    private Set<String> fetchedProxies = new HashSet<>();

    @Autowired
    private WebDriverFactory webDriverFactory;

    @Autowired
    private ProxyFetcher proxyFetcher;

    @Autowired
    private IDataSource dataSource;

    @Autowired
    private AppConfig appConfig;

    @Autowired
    private BaseScraperConfiguration scraperConfiguration;

    @Override
    public ProxyAdapter getNextWorkingProxy()
    {
        Instant start = Instant.now();
        LOGGER.info("Getting next working Proxy ... ");
        ProxyAdapter workingProxy = getWorkingProxyFromSet(proxiesInputData);
        if (workingProxy == null)
        {
            LOGGER.warn("All proxies from proxies.txt were consumed, fetching new ones ...");
            if (fetchedProxies.isEmpty())
            {
                proxyFetcher.setDriver(getDriver());
                fetchedProxies = proxyFetcher.fetchProxies();
                dataSource.writeProxiesToFile(fetchedProxies);
            }
            workingProxy = getWorkingProxyFromSet(fetchedProxies);
            if (workingProxy == null)
            {
                LOGGER.error("All proxy sources were consumed, trying to refresh them ...");
                while (Duration.between(start, Instant.now()).toMinutes() < 10)
                {
                    LOGGER.info("Waiting for 10 minutes before fetching new proxies...");
                    WaitUtils.sleepSeconds(this, 60 * 10);
                }
                return getNextWorkingProxy();
            }
            dataSource.writeProxiesToFile(fetchedProxies);
            return workingProxy;
        }
        dataSource.writeProxiesToFile(proxiesInputData);
        return workingProxy;
    }

    @PostConstruct
    private void initProxiesList()
    {
        proxiesInputData = FileUtils.readFileToLines("settings/proxies.txt");
    }

    public void setWebDriverFactory(WebDriverFactory webDriverFactory)
    {
        this.webDriverFactory = webDriverFactory;
    }

    private ProxyAdapter buildProxy(String address)
    {
        Proxy seleniumProxy = new Proxy().setHttpProxy(address).setFtpProxy(address).setSslProxy(address);
        String[] parts = address.split(":");
        return new ProxyAdapter(seleniumProxy, parts[0], Integer.parseInt(parts[1]));
    }

    private WebDriver getDriver()
    {
        return webDriverFactory.create(DriverType.CHROME);
    }

    private WebDriver getDriver(ProxyAdapter proxy)
    {
        return webDriverFactory.create(proxy, DriverType.CHROME);
    }

    private ProxyAdapter getWorkingProxyFromSet(Set<String> inputData)
    {
        Iterator<String> iterator = inputData.iterator();
        while (iterator.hasNext())
        {
            String address = iterator.next();
            if (!isAddressValid(address))
            {
                iterator.remove();
                continue;
            }
            ProxyAdapter proxy = buildProxy(address);
            if (!isProxyWorking(proxy))
            {
                iterator.remove();
                continue;
            }
            LOGGER.info("Proxy {} is working and will be used in further requests", proxy);
            return proxy;
        }
        LOGGER.warn("Input proxies data is empty, returning NULL proxy");
        return null;
    }

    private boolean isAddressValid(String address)
    {
        return VALID_PROXY_ADDRESS_PATTERN.matcher(address).matches();
    }

    private boolean isProxyFastEnough(Duration pageLoad)
    {
        long actualResponseTime = pageLoad.toMillis();
        LOGGER.info("Validating connection speed to the proxy: {} millis", actualResponseTime);
        return actualResponseTime <= scraperConfiguration.getMaxProxyResponseTime();
    }

    private boolean isProxyReachable(ProxyAdapter proxy)
    {
        try
        {
            return InetAddress.getByName(proxy.getHost()).isReachable(500);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while checking if proxy reachable: {}", e.getMessage());
            return false;
        }
    }

    private boolean isProxyWorking(ProxyAdapter proxy)
    {
        LOGGER.info("Checking if Proxy with address {} is working ...", proxy);
        if (!isProxyReachable(proxy))
        {
            LOGGER.info("No, it's not: proxy not reachable");
            return false;
        }
        WebDriver driver = getDriver(proxy);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        try
        {
            return isProxyWorkingAndFast(driver);
        }
        catch (WebDriverException e)
        {
            LOGGER.info("No, it's not: normal page was not loaded");
            return false;
        }
        finally
        {
            driver.quit();
        }
    }

    private boolean isProxyWorkingAndFast(WebDriver driver)
    {
        Instant start = Instant.now();
        ScrapTarget scrapTarget = appConfig.getScrapTarget();
        driver.navigate().to(scrapTarget.getMainPageUrl());
        Duration pageLoadTime = Duration.between(start, Instant.now());
        WebElement navBar = driver.findElement(By.xpath(scrapTarget.getNormalPageElementXpath()));
        boolean isWorkingAndFast = navBar != null && isProxyFastEnough(pageLoadTime);
        LOGGER.info("Proxy is reachable and fast enough: {}", isWorkingAndFast);
        return isWorkingAndFast;
    }
}
