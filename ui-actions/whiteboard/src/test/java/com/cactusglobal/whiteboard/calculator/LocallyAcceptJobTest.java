package com.cactusglobal.whiteboard.calculator;

import com.cactusglobal.whiteboard.ScheduledTask;
import com.cactusglobal.whiteboard.WebDriverProvider;
import com.cactusglobal.whiteboard.action.UiActions;
import com.cactusglobal.whiteboard.model.Job;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class LocallyAcceptJobTest
{
    //@Test
    public void testAcceptFunctionality() throws IOException
    {
        WebDriver driver = WebDriverProvider.getDriverInstance();
        driver.navigate().to("file:///D:/GitRepos/freelance/downwork/ui-actions/whiteboard/test.html");

        ScheduledTask task = new ScheduledTask();

        Document doc = Jsoup.parse(new File("test.html"), "utf-8");
        Element allocatedJobs = doc.getElementById("fl_job_allocation");
        Elements allocatedJobsContent = allocatedJobs.getElementsByClass("dashboardBlockContent");
        if (!allocatedJobsContent.text().contains("No new jobs assigned"))
        {
            System.out.println();
            System.out.println("=======================================");
            System.out.println("!!! Found Allocated Jobs! Processing...");
            List<Job> jobs = task.getAllocatedJobs(allocatedJobs);
            int dailyCapacity = 2660;
            CapacityCalculator calculator = new CapacityCalculator(dailyCapacity);
            for (Job job : jobs)
            {
                if (calculator.canTakeJob(job))
                {
                    System.out.println("Trying to accept the job: " + job);
                    UiActions uiActions = new UiActions(WebDriverProvider.getDriverInstance());
                    if (uiActions.acceptJob(job))
                    {
                        System.out.println("Add new job to Ongoing list: " + job);
                    }
                }
            }
        }

    }
}
