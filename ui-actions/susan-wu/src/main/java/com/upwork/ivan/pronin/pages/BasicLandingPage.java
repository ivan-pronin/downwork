package com.upwork.ivan.pronin.pages;

import com.upwork.ivan.pronin.ElementActions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class BasicLandingPage
{
    private WebDriver driver;
    private ElementActions elementActions;

    public BasicLandingPage(WebDriver driver, ElementActions elementActions)
    {
        this.driver = driver;
        this.elementActions = elementActions;
    }

    protected void login(String emailId, String passId, String email, String password, String submitId)
    {
        WebElement emailInput = elementActions.waitForElement(emailId);
        elementActions.enterTextToElement(email, emailInput);
        WebElement passInput = driver.findElement(By.id(passId));
        elementActions.enterTextToElement(password, passInput);
        elementActions.click(By.id(submitId));
    }
}
