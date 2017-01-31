package com.upwork.ivan.pronin.pages;

import com.upwork.ivan.pronin.ElementActions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RaisePage extends BasicLandingPage
{
    private ElementActions elementActions;

    public RaisePage(WebDriver driver, ElementActions elementActions)
    {
        super(driver, elementActions);
        this.elementActions = elementActions;
    }

    public void login(String email, String password)
    {
        login("user_email", "user_password", email, password, "login-button");
    }

    public void getStartedWithNewStore(String text)
    {
        WebElement storeName = elementActions.waitForElement("listing_store_name");
        elementActions.enterTextToElement(text, storeName);
        elementActions.click(By.xpath("//input[@value='Get Started']"));
        handleSuggestions();
    }

    private void handleSuggestions()
    {
        WebElement suggestionLink = elementActions
                .waitForElement(By.xpath("//p[@class='listing-suggestion']//a[@rel='nofollow']"), 5);
        if (suggestionLink != null)
        {
            System.out.println("Suggestion appeared, clicking on it");
            suggestionLink.click();
        }
    }

    public void createNewListing(String cardSerialNumber, String pinNumber, String costPrice, String listingPrice)
    {
        WebElement listingAccountNumber = elementActions.waitForElement("listing_account_number");
        elementActions.enterTextToElement(cardSerialNumber, listingAccountNumber);
        elementActions.enterTextById("listing_pin_number", pinNumber);
        elementActions.enterTextById("listing_cost_price", costPrice);
        elementActions.enterTextById("listing_price", listingPrice);
        elementActions.click(By.id("new-listing-next-step"));
    }
}
