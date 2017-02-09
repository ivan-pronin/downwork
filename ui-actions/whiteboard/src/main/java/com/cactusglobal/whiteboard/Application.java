package com.cactusglobal.whiteboard;

import com.cactusglobal.whiteboard.util.PropertiesLoader;
import com.cactusglobal.whiteboard.util.WebDriverUtil;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Timer;

public class Application
{
    public static final String MAIN_PAGE = "http://whiteboard.cactusglobal.com";

    public static void main(String[] args)
    {
        System.out.println("Program started at: " + ScheduledTask.getTimeStamp());
        Properties props = PropertiesLoader.getProperties();
        WebDriver driver = WebDriverProvider.getDriverInstance();
        driver.navigate().to(MAIN_PAGE);

        ElementActions elementActions = new ElementActions(driver, 30);
        elementActions.enterTextById("edit-name-1", props.getProperty("login"));
        elementActions.enterTextById("edit-pass-1", props.getProperty("password"));
        elementActions.click(By.id("edit-submit-1"));
        WebDriverUtil.waitForPageToLoad(driver);
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
