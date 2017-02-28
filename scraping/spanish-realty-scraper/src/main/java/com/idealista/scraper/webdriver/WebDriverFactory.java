package com.idealista.scraper.webdriver;

import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import com.idealista.scraper.proxy.ProxyAdapter;
import com.idealista.scraper.util.PropertiesLoader;
import com.machinepublishers.jbrowserdriver.JBrowserDriver;
import com.machinepublishers.jbrowserdriver.ProxyConfig;
import com.machinepublishers.jbrowserdriver.ProxyConfig.Type;
import com.machinepublishers.jbrowserdriver.Settings;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

public class WebDriverFactory implements IWebDriverFactory
{
    private static final Properties PROPERTIES = PropertiesLoader.getProperties();
    private static final TimeUnit TIME_UNIT_SECONDS = TimeUnit.SECONDS;
    private static final int PAGE_LOAD_TIMEOUT = 60;

    @Override
    public WebDriver create(DriverType type)
    {
        return create(new ProxyAdapter(), type);
    }

    @Override
    public WebDriver create(ProxyAdapter proxy, DriverType type)
    {
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        ChromeOptions options = new ChromeOptions();
        cap.setCapability(CapabilityType.PROXY, proxy.getSeleniumProxy());

        if (Boolean.parseBoolean(PROPERTIES.getProperty("useLightweightedChrome")))
        {
            // this disables images loading
            HashMap<String, Object> images = new HashMap<String, Object>();
            images.put("images", 2);
            HashMap<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values", images);
            options.setExperimentalOption("prefs", prefs);
            
            options.addArguments("incognito", "disable-extensions", "disable-plugins", "test-type", "no-sandbox",
                    "enable-strict-powerful-feature-restrictions");
            cap.setCapability(ChromeOptions.CAPABILITY, options);
        }

        if (!Boolean.parseBoolean(PROPERTIES.getProperty("enableJavascript")))
        {
            cap.setJavascriptEnabled(false);
        }

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
        timeouts.setScriptTimeout(PAGE_LOAD_TIMEOUT, TIME_UNIT_SECONDS);
        timeouts.pageLoadTimeout(PAGE_LOAD_TIMEOUT, TIME_UNIT_SECONDS);
        if (Boolean.getBoolean(PROPERTIES.getProperty("maximizeBrowserWindow")))
        {
            manage.window().maximize();
        }
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
