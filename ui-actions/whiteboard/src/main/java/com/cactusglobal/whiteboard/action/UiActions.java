package com.cactusglobal.whiteboard.action;

import java.util.List;

import com.cactusglobal.whiteboard.model.Job;
import com.cactusglobal.whiteboard.util.WebDriverUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class UiActions
{
    private static final Logger LOGGER = LogManager.getLogger(UiActions.class);
    
    private WebDriver driver;

    public UiActions(WebDriver driver)
    {
        this.driver = driver;
    }

    public boolean acceptJob(Job job)
    {
        refreshPage();
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
                    LOGGER.info("Clicked Accept link");
                    WebDriverUtil.takeScreenShot();
                    WebDriverWait wait = new WebDriverWait(driver, 5);
                    try
                    {
                        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
                        if (alert != null)
                        {
                            LOGGER.info("Alert appeared");
                            WebDriverUtil.takeScreenShot();
                            driver.switchTo().alert().accept();
                            LOGGER.info("Alert was accepted");
                            LOGGER.info("The job has just been accepted: {}!!!", job);
                            WebDriverUtil.takeScreenShot();
                            return true;
                        }
                    }
                    catch (TimeoutException e)
                    {
                        LOGGER.error("Failed to wait for alert: {}", e);
                    }
                }
            }
        }
        return false;
    }

    private void refreshPage()
    {
        LOGGER.info("Refreshing current page in browser");
        driver.navigate().refresh();
        LOGGER.info("Page was refreshed");
    }
}
