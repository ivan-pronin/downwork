package com.cactusglobal.whiteboard;

import com.cactusglobal.whiteboard.action.ElementActions;
import com.cactusglobal.whiteboard.util.WebDriverUtil;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ScreenshotTests
{
    private static final String MAIN_PAGE = "http://whiteboard.cactusglobal.com";
    
    //@Test
    public void testTakeScreenshot() throws Exception
    {
        WebDriver driver = WebDriverProvider.getDriverInstance();
        driver.navigate().to(MAIN_PAGE);
        WebDriverUtil.takeScreenShot();
        ElementActions elementActions = new ElementActions(driver, 30);
        elementActions.enterTextById("edit-name-1", "soursofgrain@gmail.com");
        elementActions.enterTextById("edit-pass-1", "hamster$");
        elementActions.click(By.id("edit-submit-1"));
        WebDriverUtil.waitForPageToLoad(driver);
        WebDriverUtil.takeScreenShot();
    }
}
