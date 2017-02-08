package com.upwork.ivan.pronin.pages;

import com.upwork.ivan.pronin.ElementActions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class TopCashBackPage extends BasicLandingPage
{
    private static final String GET_CASHBACK_BUTTON_LOCATOR = "//a[contains(@id,'ctl00_GeckoTwoColPrimary_merchantPnl_rMerchantOffers') and text()='Get Cashback']";
    private ElementActions elementActions;

    public TopCashBackPage(WebDriver driver, ElementActions elementActions)
    {
        super(driver, elementActions);
        this.elementActions = elementActions;
    }

    public void login(String email, String password)
    {
        login("ctl00_GeckoOneColPrimary_Login_txtEmail", "ctl00_GeckoOneColPrimary_Login_txtPassword", email, password,
                "ctl00_GeckoOneColPrimary_Login_Loginbtn");
    }

    public void clickFirstCashBack()
    {
        elementActions.click(By.xpath(GET_CASHBACK_BUTTON_LOCATOR));
    }

    public void clickGetCashBackButtonByIndex(int index)
    {
        elementActions.click(By.xpath("(" + GET_CASHBACK_BUTTON_LOCATOR + ")" + "[" + index + "]"));
    }
}
