package com.upwork.ivan.pronin;

import java.util.Arrays;
import java.util.List;

import com.upwork.ivan.pronin.pages.RaisePage;
import com.upwork.ivan.pronin.pages.TopCashBackPage;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class Application
{
    private static final String TOP_CASHBACK = "https://www.topcashback.com/";

    public static void main(String[] args)
    {
        WebDriver driver = WebDriverProvider.getDriverInstance();
        
        driver.navigate().to(TOP_CASHBACK + "login");
        ElementActions elementActions = new ElementActions(driver, 30);
        TopCashBackPage topCashBackPage = new TopCashBackPage(driver, elementActions);
        topCashBackPage.login("smicschool@gmail.com", "smicschool");

        String firstTab = driver.getWindowHandle();
        
        WebDriverUtil.openNewTab(driver, "https://www.raise.com/user/sign_in");
        WebDriverUtil.switchToOtherTab(driver, Arrays.asList(firstTab));
        
        RaisePage raisePage = new RaisePage(driver, elementActions);
        String secondTab = driver.getWindowHandle();
        raisePage.login("smicschool@gmail.com", "smicschool_1234");
        
        WebDriverUtil.switchToOtherTab(driver, Arrays.asList(secondTab));
        
        driver.navigate().to(TOP_CASHBACK + "raise");
        topCashBackPage.clickFirstCashBack();

        WebDriverUtil.switchToOtherTab(driver, Arrays.asList(firstTab, secondTab));
        
        raisePage.getStartedWithNewStore("storeName");
        
        // last form
        raisePage.createNewListing("6003861145394746", "733", "25", "24");
        WebDriverProvider.stopDriver();
    }


}
