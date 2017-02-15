package com.cactusglobal.whiteboard;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;

import com.cactusglobal.whiteboard.action.ElementActions;
import com.cactusglobal.whiteboard.util.PropertiesLoader;
import com.cactusglobal.whiteboard.util.WebDriverUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

public class Application
{
    public static final String MAIN_PAGE = "http://whiteboard.cactusglobal.com";
    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    public static void main(String[] args)
    {
        LOGGER.info("Program started");
        Properties props = PropertiesLoader.getProperties();
        WebDriver driver = WebDriverProvider.getDriverInstance();
        driver.navigate().to(MAIN_PAGE);

        ElementActions elementActions = new ElementActions(driver, 30);
        elementActions.enterTextById("edit-name-1", props.getProperty("login"));
        elementActions.enterTextById("edit-pass-1", props.getProperty("password"));
        elementActions.click(By.id("edit-submit-1"));
        WebDriverUtil.waitForPageToLoad(driver);
        WebDriverUtil.takeScreenShot();
        Set<Cookie> cookies = driver.manage().getCookies();

        Map<String, String> sessionCookies = new LinkedHashMap<>();
        for (Cookie cookie : cookies)
        {
            String name = cookie.getName();
            if (name.contains("AWSALB") || name.contains("SESS"))
            {
                sessionCookies.put(name, cookie.getValue());
            }
        }
        ScheduledTask task = new ScheduledTask();
        task.setCookies(sessionCookies);

        Timer timer = new Timer();
        int timeoutSeconds = Integer
                .parseInt(PropertiesLoader.getProperties().getProperty("pageRefreshTimeoutSeconds", "60"));
        timer.schedule(task, 0, timeoutSeconds * 1000);
    }
}
