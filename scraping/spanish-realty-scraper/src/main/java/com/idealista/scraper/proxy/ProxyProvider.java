package com.idealista.scraper.proxy;

import com.idealista.scraper.util.FileUtils;
import com.idealista.scraper.webdriver.WebDriverFactory;
import com.idealista.scraper.webdriver.WebDriverFactory.DriverType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Proxy;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Pattern;

public class ProxyProvider implements IProxyProvider
{
    private static final Logger LOGGER = LogManager.getLogger(ProxyProvider.class);
    private static final String IDEALISTA_COM = "https://www.idealista.com";

    public static final Pattern VALID_PROXY_ADDRESS_PATTERN = Pattern
            .compile("\\b(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}"
                    + "(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\b:\\d{2,5}");

    private Set<String> proxiesInputData = new HashSet<>();

    public ProxyProvider()
    {
        initProxiesList();
    }

    private void initProxiesList()
    {
        proxiesInputData = FileUtils.readFileToLines("proxies.txt");
    }

    @Override
    public ProxyAdapter getNextWorkingProxy()
    {
        LOGGER.info("Getting next working Proxy ... ");
        Iterator<String> iterator = proxiesInputData.iterator();
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
        return null;
    }

    private boolean isProxyWorking(ProxyAdapter proxy)
    {
        LOGGER.info("Checking if Proxy with address {} is working ...", proxy);
        WebDriverFactory webDriverFactory = new WebDriverFactory();
        WebDriver driver = webDriverFactory.create(proxy, DriverType.CHROME);
        try
        {
            driver.navigate().to(IDEALISTA_COM);
            WebElement navBar = driver.findElement(By.id("no-login-user-bar"));
            return navBar != null;
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

    private boolean isAddressValid(String address)
    {
        return VALID_PROXY_ADDRESS_PATTERN.matcher(address).matches();
    }

    private ProxyAdapter buildProxy(String address)
    {
        Proxy seleniumProxy = new Proxy().setHttpProxy(address).setFtpProxy(address).setSslProxy(address);
        String[] parts = address.split(":");
        return new ProxyAdapter(seleniumProxy, parts[0], Integer.parseInt(parts[1]));
    }
}
