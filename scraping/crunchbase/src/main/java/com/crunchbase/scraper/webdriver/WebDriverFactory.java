package com.crunchbase.scraper.webdriver;

import com.crunchbase.scraper.util.WebDriverUtils;
import com.crunchbase.scraper.webdriver.proxy.ProxyAdapter;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriver.Options;
import org.openqa.selenium.WebDriver.Timeouts;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxBinary;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

@Component
public class WebDriverFactory implements IWebDriverFactory
{
    private static final Logger LOGGER = LogManager.getLogger(WebDriverFactory.class);
    private static final TimeUnit TIME_UNIT_SECONDS = TimeUnit.SECONDS;
    private static final int PAGE_LOAD_TIMEOUT = 150;

    private static ChromeDriverService service;

    @Value("${useDriverService}")
    private boolean useChromeService;

    @Value("${useLightweightedChrome}")
    private boolean useLightweightedChrome;

    @Value("${enableJavascript}")
    private boolean enableJavascript;

    @Value("${maximizeBrowserWindow}")
    private boolean maximizeBrowserWindow;

    public enum DriverType
    {
        HTMLUNIT, CHROME, JBROWSER, FIREFOX;

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

        if (useLightweightedChrome)
        {
            // this disables images loading
            HashMap<String, Object> images = new HashMap<String, Object>();
            images.put("images", 2);
            HashMap<String, Object> prefs = new HashMap<String, Object>();
            prefs.put("profile.default_content_setting_values", images);
            prefs.put("profile.default_content_settings.cookies", 2);
            options.setExperimentalOption("prefs", prefs);

            options.addArguments("incognito", "disable-extensions", "disable-plugins", "test-type", "no-sandbox",
                    "enable-strict-powerful-feature-restrictions");
            options.addArguments("disable-local-storage");
            cap.setCapability(ChromeOptions.CAPABILITY, options);
        }

        if (!enableJavascript)
        {
            cap.setJavascriptEnabled(false);
            cap.setCapability("chrome.switches", Arrays.asList("--disable-javascript"));
        }

        if (useChromeService && service == null)
        {
            service = ChromeDriverService.createDefaultService();
            try
            {
                service.start();
            }
            catch (IOException e)
            {
                LOGGER.error("Failed to start ChromeDriverService: {}", e);
                return null;
            }
        }

        WebDriver driver = null;

        FirefoxProfile profile = new FirefoxProfile();
        FirefoxBinary binary = WebDriverUtils.getRandomBinary();

        // disable auto updates
        profile.setPreference("app.update.auto", false);
        profile.setPreference("app.update.enabled", false);
        profile.setPreference("app.update.silent", false);
//        profile.setPreference("app.update.service.enabled", false);
//        profile.setPreference("app.update.staging.enabled", false);

        // disable images loading
//        profile.setPreference("permissions.default.image", 2);
        
        FirefoxOptions ffOptions = new FirefoxOptions();
        ffOptions.setBinary(binary);
        ffOptions.setProfile(profile);
        // profile.setPreference("general.useragent.override", "MyBrowser1");
        // profile.setPreference("network.cookie.cookieBehavior", 2);
        // profile.setPreference("browser.cache.disk.enable", false);
        // profile.setPreference("browser.cache.memory.enable", false);
        // profile.setPreference("browser.cache.offline.enable", false);
        // profile.setPreference("network.http.use-cache", false);
        switch (type)
        {
            case CHROME:
                if (useChromeService)
                {
                    driver = new RemoteWebDriver(service.getUrl(), cap);
                    break;
                }
                driver = new ChromeDriver(cap);
                break;
            case HTMLUNIT:
                driver = new HtmlUnitDriver();
                break;
            case FIREFOX:
                driver = new FirefoxDriver(ffOptions);
                break;
            default:
                driver = new HtmlUnitDriver();
                break;
        }
        Options manage = driver.manage();
        Timeouts timeouts = manage.timeouts();
        timeouts.setScriptTimeout(PAGE_LOAD_TIMEOUT, TIME_UNIT_SECONDS);
        timeouts.pageLoadTimeout(PAGE_LOAD_TIMEOUT, TIME_UNIT_SECONDS);
        if (maximizeBrowserWindow)
        {
            manage.window().maximize();
        }
        return driver;
    }
}
