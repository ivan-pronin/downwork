package com.cactusglobal.whiteboard;

import com.cactusglobal.whiteboard.model.Job;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class UiActions
{
    private WebDriver driver;

    public UiActions(WebDriver driver)
    {
        this.driver = driver;
    }

    public boolean acceptJob(Job job)
    {
        driver.navigate().refresh();
        ElementActions actions = new ElementActions(driver, 10);
        WebElement div = actions.waitForElement("fl_job_allocation");
        List<WebElement> rows = div.findElements(By.xpath(".//table//tbody//tr"));
        for (WebElement row : rows)
        {
            if (row.findElement(By.xpath(".//td")).getText().contains(job.getCode()))
            {
                WebElement acceptLink = row.findElement(By.xpath(".//input[@name='acceptJob']"));
                if (acceptLink != null)
                {
                    acceptLink.click();
                    WebDriverWait wait = new WebDriverWait(driver, 5);
                    try
                    {
                        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                        if (alert != null)
                        {
                            driver.switchTo().alert().accept();
                            System.out.println(job + " has just been accepted!!!");
                            return true;
                        }
                        
                    }
                    catch (TimeoutException e)
                    {
                        System.err.println("Failed to wait for alert: " + e.getMessage());
                    }
                }
            }
        }
        return false;
    }
}
