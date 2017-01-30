package com.upwork.ivan.pronin;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Application
{
    private static final String TOP_CASHBACK = "https://www.topcashback.com/";

    public static void main(String[] args)
    {
        WebDriver driver = WebDriverProvider.getDriverInstance();
        
        driver.navigate().to(TOP_CASHBACK + "login");
//        WebDriverUtil.waitForPageToLoad(driver);
        login(driver, "ctl00_GeckoOneColPrimary_Login_txtEmail", "ctl00_GeckoOneColPrimary_Login_txtPassword",
                "smicschool@gmail.com", "smicschool", "ctl00_GeckoOneColPrimary_Login_Loginbtn");

        String firstTab = driver.getWindowHandle();
        System.out.println("First tab: " + firstTab);
        WebDriverUtil.openNewTab(driver, "https://www.raise.com/user/sign_in");
        switchToOtherTab(driver, Arrays.asList(firstTab));
        String secondTab = driver.getWindowHandle();
        
        login(driver, "user_email", "user_password", "smicschool@gmail.com", "smicschool_1234", "login-button");

        switchToOtherTab(driver, Arrays.asList(secondTab));
        driver.navigate().to(TOP_CASHBACK + "raise");
        WebElement getCashback = driver.findElement(By.xpath(
                "//a[contains(@id,'ctl00_GeckoTwoColPrimary_merchantPnl_rMerchantOffers') and text()='Get Cashback']"));
        System.out.println(getCashback.getText());
        getCashback.click(); // 3rd tab opens

        switchToOtherTab(driver, Arrays.asList(firstTab, secondTab));
        
        WebElement storeName = waitForElement(driver, "listing_store_name", 30);
        storeName.clear();
        storeName.sendKeys("Banana Republic");
        
        driver.findElement(By.xpath("//input[@value='Get Started']")).submit();
        
        // last form
        
        WebElement listingAccountNumber = waitForElement(driver, "listing_account_number", 30);
        enterText(listingAccountNumber, "6003861145394746");
        enterTextById(driver, "listing_pin_number", "6003861145394746");
        enterTextById(driver, "listing_cost_price", "733");
        enterTextById(driver, "listing_price", "25");

        driver.findElement(By.id("new-listing-next-step")).submit();
        
        WebDriverProvider.stopDriver();
    }

    private static void enterTextById(WebDriver driver, String id, String text)
    {
        WebElement element = driver.findElement(By.id(id));
        element.clear();
        element.sendKeys(text);
    }

    private static void enterText(WebElement listingAccountNumber, String text)
    {
        listingAccountNumber.clear();
        listingAccountNumber.sendKeys(text);
    }

    private static void switchToOtherTab(WebDriver driver, List<String> prevoiusTabs)
    {
        for (String tab : driver.getWindowHandles())
        {
            if (!prevoiusTabs.contains(tab))
            {
                driver.switchTo().window(tab);
            }
        }
    }

    private static void login(WebDriver driver, String emailId, String passId, String email, String pass,
            String submitId)
    {
        WebElement emailInput = waitForElement(driver, emailId, 30);
        emailInput.clear();
        emailInput.sendKeys(email);
        WebElement passInput = driver.findElement(By.id(passId));
        passInput.clear();
        passInput.sendKeys(pass);
        driver.findElement(By.id(submitId)).click();
    }

    private static WebElement waitForElement(WebDriver driver, String elementId, int seconds)
    {
        WebElement emailInput = (new WebDriverWait(driver, seconds))
                .until(ExpectedConditions.presenceOfElementLocated(By.id(elementId)));
        return emailInput;
    }
}
