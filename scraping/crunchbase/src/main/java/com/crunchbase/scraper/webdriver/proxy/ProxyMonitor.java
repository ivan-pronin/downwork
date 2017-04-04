package com.crunchbase.scraper.webdriver.proxy;

import com.crunchbase.scraper.ui.StartPage;
import com.crunchbase.scraper.util.WebDriverUtils;
import com.crunchbase.scraper.webdriver.WebDriverProvider;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class ProxyMonitor
{
    private static final Logger LOGGER = LogManager.getLogger();

    @Autowired
    private WebDriverProvider webDriverProvider;

    public WebDriver checkForVerificationAndRestartDriver(WebDriver driver)
    {
        if (ifVerificationAppered(driver))
        {
            LOGGER.info("Verification page is opened, restarting driver");
            return restartDriver();
        }
        return driver;
    }

    public boolean ifVerificationAppered(WebDriver driver)
    {
        boolean isBlocked = driver.getTitle().contains("Pardon Our Interruption");
        if (isBlocked)
        {
            LOGGER.error("Verification page appeared");
            WebDriverUtils.waitForPageLoad(driver);
            List<WebElement> captcha = driver.findElements(By.id("distilCaptchaForm"));
            if (!captcha.isEmpty())
            {
                LOGGER.error("!!! CAPTHCA APPEARED !!!");
                LOGGER.error("Start waiting until captcha is resolved");
//                WaitUtils.sleepSeconds(this, 60);
//                driver.manage().deleteAllCookies();
                StartPage page = new StartPage();
                page.setWebDriver(driver);
                driver.navigate().refresh();
                page.waitForSearchToLoad();
                if (driver.getTitle().contains("Pardon Our Interruption"))
                {
                    return true;
                }
                return false;
            }
        }
        return isBlocked;
    }

    public WebDriver restartDriver()
    {
        webDriverProvider.end();
        return webDriverProvider.get();
    }

    public void setWebDriverProvider(WebDriverProvider webDriverProvider)
    {
        this.webDriverProvider = webDriverProvider;
    }
}
