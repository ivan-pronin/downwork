package com.crunchbase.scraper.webdriver.proxy;

import com.crunchbase.scraper.data.IDataSource;
import com.crunchbase.scraper.service.CrunchbaseScraperService;
import com.crunchbase.scraper.ui.SearchActions;
import com.crunchbase.scraper.ui.StartPage;
import com.crunchbase.scraper.util.FileUtils;
import com.crunchbase.scraper.webdriver.WebDriverFactory;
import com.crunchbase.scraper.webdriver.WebDriverFactory.DriverType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

import java.io.IOException;
import java.net.InetAddress;
import java.time.Duration;
import java.time.Instant;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

@Component
public class ProxyProvider implements IProxyProvider
{
    private static final Logger LOGGER = LogManager.getLogger(ProxyProvider.class);

    public static final Pattern VALID_PROXY_ADDRESS_PATTERN = Pattern
            .compile("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
                    + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b:\\d{2,5}");

    private Set<String> proxiesInputData = new HashSet<>();
    private Set<String> fetchedProxies = new HashSet<>();

    private DriverType driverType;

    @Autowired
    private WebDriverFactory webDriverFactory;

    @Autowired
    private ProxyFetcher proxyFetcher;

    @Autowired
    private IDataSource dataSource;

    @Value("${maxProxyResponseTime}")
    private long maxProxyResponseTime;

    @Value("${driverType}")
    private String driverTypeString;

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
                    synchronized (this)
                    {
                        try
                        {
                            this.wait(10 * 60 * 1000);
                        }
                        catch (InterruptedException e)
                        {
                            LOGGER.error("Failed to wait: {}", e.getMessage());
                        }
                    }
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
        proxiesInputData = FileUtils.readFileToLines("./proxies.txt");
        driverType = DriverType.fromString(driverTypeString);
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
        return webDriverFactory.create(driverType);
    }

    private WebDriver getDriver(ProxyAdapter proxy)
    {
        return webDriverFactory.create(proxy, driverType);
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
        return actualResponseTime <= maxProxyResponseTime;
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
            LOGGER.info("No, it's not.");
            return false;
        }
        WebDriver driver = getDriver(proxy);
        driver.manage().timeouts().pageLoadTimeout(30, TimeUnit.SECONDS);
        try
        {
            Instant start = Instant.now();
            StartPage page = new StartPage();
            page.setWebDriver(driver);
            driver.navigate().to(CrunchbaseScraperService.CRUNCHBASE_COM);
            page.waitToLoadFast();
            LOGGER.info("loaded");
            Duration pageLoadTime = Duration.between(start, Instant.now());
            WebElement navBar = driver.findElement(By.id("q"));
            boolean isWorkingAndFast = navBar != null && isProxyFastEnough(pageLoadTime);
            LOGGER.info("Proxy is reachable and fast enough: {}", isWorkingAndFast);
            return isWorkingAndFast;
        }
        catch (WebDriverException e)
        {
            LOGGER.info("No, it's not.");
            return false;
        }
        finally
        {
            driver.quit();
        }
    }
}
