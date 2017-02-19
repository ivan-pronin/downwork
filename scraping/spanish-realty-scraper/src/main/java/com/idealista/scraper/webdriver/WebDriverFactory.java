package com.idealista.scraper.webdriver;

import java.util.concurrent.TimeUnit;

import com.idealista.scraper.proxy.ProxyAdapter;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.ProxyConfig;
import com.machinepublishers.jbrowserdriver.ProxyConfig.Type;
import com.machinepublishers.jbrowserdriver.Settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverFactory implements IWebDriverFactory
{
    private static final int PAGE_LOAD_TIMEOUT = 60;
    private static final int ELEMENT_WAIT_TIMEOUT = 10;
    private static final TimeUnit TIME_UNIT_SECONDS = TimeUnit.SECONDS;

    @Override
    public WebDriver create(DriverType type)
    {
        return create(new ProxyAdapter(), type);
    }

    @Override
    public WebDriver create(ProxyAdapter proxy, DriverType type)
    {
        DesiredCapabilities cap = new DesiredCapabilities();
        cap.setCapability(CapabilityType.PROXY, proxy.getSeleniumProxy());

        ProxyConfig proxyConfig = new ProxyConfig(Type.HTTP, proxy.getHost(), proxy.getPort());
        Settings settings = Settings.builder().proxy(proxyConfig).build();
        WebDriver driver = null;
        switch (type)
        {
            case CHROME:
                driver = new ChromeDriver(cap);
                break;
            case JBROWSER:
                driver = new JBrowserDriver(settings);
                break;
            default:
                driver = new HtmlUnitDriver();
                break;
        }
        Options manage = driver.manage();
        Timeouts timeouts = manage.timeouts();
        timeouts.pageLoadTimeout(PAGE_LOAD_TIMEOUT, TIME_UNIT_SECONDS);
        manage.window().maximize();
        return driver;
    }

    public enum DriverType
    {
        HTMLUNIT, CHROME, JBROWSER;

        public static DriverType fromString(String str)
        {
            for (DriverType type : DriverType.values())
            {
                if (str.equalsIgnoreCase(type.name()))
                {
                    return type;
                }
            }
            return null;
        }
    }
}
