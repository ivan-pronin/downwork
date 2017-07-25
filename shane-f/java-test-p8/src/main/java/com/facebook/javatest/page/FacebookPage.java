package com.facebook.javatest.page;

import java.util.Arrays;
import java.util.List;

import com.facebook.javatest.actions.ClickActions;
import com.facebook.javatest.actions.ElementActions;
import com.facebook.javatest.actions.SearchActions;
import com.facebook.javatest.actions.WaitActions;
import com.facebook.javatest.util.WaitUtils;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class FacebookPage
{
    private static final int ELEMENT_WAIT_TIMEOUT_SECONDS = 10;
    private ClickActions clickActions = new ClickActions();
    private ElementActions elementActions = new ElementActions();
    private SearchActions searchActions = new SearchActions();
    private WaitActions waitActions = new WaitActions();

    public FacebookPage(WebDriver driver)
    {
        searchActions.setWebDriver(driver);
        clickActions.setWebDriver(driver);
        waitActions.setWebDriver(driver);
    }

    public void login(String login, String password)
    {
        elementActions.enterTextToElement(login, searchActions.findElementsById("email"));
        elementActions.enterTextToElement(password, searchActions.findElementsById("pass"));
        clickActions.click(searchActions.findElementsById("loginbutton"));
        waitActions.waitForElement(By.id("userNav"), ELEMENT_WAIT_TIMEOUT_SECONDS);
    }

    public void openGroupsTab()
    {
        List<WebElement> tabs = waitActions.waitForElements(By.xpath("//ul[@class='_43o4']//li//a"),
                ELEMENT_WAIT_TIMEOUT_SECONDS);
        for (WebElement tab : tabs)
        {
            if (tab.getText().equalsIgnoreCase("Groups"))
            {
                clickActions.click(tab);
                return;
            }
        }
    }

    public void search(String searchQuery)
    {
        WebElement searchField = waitActions.waitForElement(By.xpath("//input[@placeholder='Search Facebook']"),
                ELEMENT_WAIT_TIMEOUT_SECONDS);
        elementActions.enterTextToElement(searchQuery, Arrays.asList(searchField));
        WaitUtils.sleepSeconds(this, 3);
        clickActions.click(searchActions.findElementsByXpath("//button[@aria-label='Search']"));
        waitActions.waitForElementDisappear(By.id("userNav"), ELEMENT_WAIT_TIMEOUT_SECONDS);
        waitActions.waitForElement(By.id("u_ps_jsonp_2_1_0"), ELEMENT_WAIT_TIMEOUT_SECONDS * 2);
    }
}
