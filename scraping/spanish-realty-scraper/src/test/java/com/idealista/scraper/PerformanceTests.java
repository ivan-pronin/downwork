package com.idealista.scraper;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;

public class PerformanceTests
{
    // private String baseUrl = "https://www.idealista.com/venta-viviendas/a-coruna-provincia/";
    private String baseUrl = "https://www.tut.by/";

    // @Test
    public void testChromePerformance()
    {
        testPerformance(new ChromeDriver());
    }

    // @Test
    public void testPhantomJSDriverPerformance()
    {
        DesiredCapabilities caps = DesiredCapabilities.phantomjs();
        caps.setCapability(PhantomJSDriverService.PHANTOMJS_EXECUTABLE_PATH_PROPERTY,
                "F:\\Vano\\DEV\\SOFT\\phantomjs\\bin\\phantomjs.exe");
        WebDriver driver = new PhantomJSDriver(caps);
        testPerformance(driver);
    }

    private void testPerformance(WebDriver driver)
    {
        long startTime = System.currentTimeMillis();
        for (int i = 0; i < 10; i++)
        {
            checkAdsCount(baseUrl, driver);
        }
        driver.quit();
        printExecutionTime(driver.getClass().getName() + " has time: ", startTime);
    }

    private void printExecutionTime(String message, long startTime)
    {
        long endTime = System.currentTimeMillis();
        int seconds = (int) ((endTime - startTime) / 1000);
        System.out.println(message + ". Total time taken: " + seconds + " seconds.");
    }

    private static void checkAdsCount(String baseUrl, WebDriver driver)
    {
        driver.navigate().to(baseUrl);
        WebElement divContainer = driver.findElement(By.xpath("//div[@id='title_news_block']"));
        if (divContainer != null)
        {
            System.out.println("Item container found: " + divContainer);
            List<WebElement> ads = divContainer.findElements(By.xpath(".//div"));
            System.out.println("Ads count: " + ads.size());
        }
    }
}
