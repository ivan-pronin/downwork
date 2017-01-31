package com.upwork.ivan.pronin;

import java.util.Arrays;
import java.util.List;

import com.upwork.ivan.pronin.CsvUtil.CsvColumn;
import com.upwork.ivan.pronin.pages.RaisePage;
import com.upwork.ivan.pronin.pages.TopCashBackPage;

import org.apache.commons.csv.CSVRecord;
import org.openqa.selenium.WebDriver;

public class Application
{
    private static final int ELEMENT_WAIT_TIMEOUT_SEC = 60;
    private static final String TOP_CASHBACK = "https://www.topcashback.com/";

    public static void main(String[] args)
    {
        long startTime = System.currentTimeMillis();
        CsvUtil csv = new CsvUtil("input.csv");
        List<CSVRecord> records = csv.readCsvFile();
        for (CSVRecord record : records)
        {
            System.out.println("");
            System.out.println("=================Starting iteration #" + record.getRecordNumber() + "===================");
            System.out.println("");
            System.out.println("Printing CSV row data for this iteration: " + printRow(record));
            executeIteration(record.get(CsvColumn.STORE_NAME), record.get(CsvColumn.ACCOUNT_NUMBER),
                    record.get(CsvColumn.PIN), record.get(CsvColumn.COST_PRICE), record.get(CsvColumn.PRICE));
            System.out.println("====================ITERATION ENDED======================");
        }
        long endTime = System.currentTimeMillis();
        int seconds = (int) ((endTime - startTime ) / 1000);
        System.out.println("Finished all iterations, total time taken: " + seconds + " seconds.");
    }

    private static void executeIteration(String storeName, String accountNumber, String pin, String costPrice,
            String price)
    {
        WebDriver driver = WebDriverProvider.getDriverInstance();

        driver.navigate().to(TOP_CASHBACK + "login");
        ElementActions elementActions = new ElementActions(driver, ELEMENT_WAIT_TIMEOUT_SEC);
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

        raisePage.getStartedWithNewStore(storeName);
        raisePage.createNewListing(accountNumber, pin, costPrice, price);
        WebDriverUtil.waitForPageToLoad(driver);
        WebDriverProvider.stopDriver();
    }

    private static String printRow(CSVRecord record)
    {
        return getColumnString(record, CsvColumn.STORE_NAME) + ", " + getColumnString(record, CsvColumn.ACCOUNT_NUMBER)
                + ", " + getColumnString(record, CsvColumn.PIN) + ", " + getColumnString(record, CsvColumn.COST_PRICE)
                + ", " + getColumnString(record, CsvColumn.PRICE);
    }

    private static String getColumnString(CSVRecord record, CsvColumn name)
    {
        return name.name() + "=" + record.get(name);
    }
}
