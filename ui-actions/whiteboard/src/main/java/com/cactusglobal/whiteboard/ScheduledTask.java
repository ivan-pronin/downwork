package com.cactusglobal.whiteboard;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TimerTask;

import com.cactusglobal.whiteboard.action.UiActions;
import com.cactusglobal.whiteboard.calculator.CapacityCalculator;
import com.cactusglobal.whiteboard.model.Job;
import com.cactusglobal.whiteboard.model.WorkInProgress;
import com.cactusglobal.whiteboard.ui.DashboardPageProcessor;
import com.cactusglobal.whiteboard.util.FileUtils;
import com.cactusglobal.whiteboard.util.PropertiesLoader;
import com.cactusglobal.whiteboard.util.WebDriverUtil;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class ScheduledTask extends TimerTask
{
    private static final Logger LOGGER = LogManager.getLogger(Application.class);

    private Map<String, String> cookies = new HashMap<>();
    private Set<WorkInProgress> workInProgress = new HashSet<>();
    private boolean isOngoingJobsInitialized;

    @Override
    public void run()
    {
        LOGGER.info("Starting to poll the site ...");
        Document doc = getDashboardDocument();
        LOGGER.info("Polling the site finished ... ");

        if (doc == null)
        {
            LOGGER.info("Could not fetch new data from site... Skipping this iteration!");
            return;
        }

        DashboardPageProcessor dashBoardProcessor = new DashboardPageProcessor(doc);
        workInProgress = initWorkInProgress(dashBoardProcessor);

        if (dashBoardProcessor.isNewAllocatedJobAppeared())
        {
            FileUtils.writeTextToFile(doc.html());

            System.out.println();
            System.out.println("=======================================");
            LOGGER.info("!!! Found Allocated Jobs! Processing...");
            WebDriverUtil.takeScreenShot();
            List<Job> jobs = dashBoardProcessor.getAllocatedJobs();
            int dailyCapacity = Integer
                    .parseInt(PropertiesLoader.getProperties().getProperty("dailyWordsCapacity", "2660"));
            CapacityCalculator calculator = new CapacityCalculator(dailyCapacity);
            calculator.setWorkInProgress(workInProgress);
            for (Job job : jobs)
            {
                if (calculator.canTakeJob(job))
                {
                    LOGGER.info("Trying to accept the job: {}", job);
                    UiActions uiActions = new UiActions(WebDriverProvider.getDriverInstance());
                    if (uiActions.acceptJob(job))
                    {
                        workInProgress.add(new WorkInProgress(job));
                        LOGGER.info("Added new job to Ongoing list: {}", job);
                        LOGGER.info("Current ongoing jobs count: {}", workInProgress.size());
                    }
                }
            }
        }
    }

    public void setCookies(Map<String, String> cookies)
    {
        this.cookies = cookies;
    }

    private Document getDashboardDocument()
    {
        try
        {
            return Jsoup.connect(Application.MAIN_PAGE + "/dashboard").userAgent("chrome").cookies(cookies)
                    .timeout(60000).get();
        }
        catch (IOException e)
        {
            LOGGER.error("Error while reading data from dashboard: {}", e);
        }
        return null;
    }

    private Set<WorkInProgress> initWorkInProgress(DashboardPageProcessor dashBoardProcessor)
    {
        if (!isOngoingJobsInitialized && workInProgress.isEmpty())
        {
            LOGGER.info("Retrieving Ongoing Jobs list from the page...");
            Set<WorkInProgress> result = new HashSet<>();
            List<Job> jobs = dashBoardProcessor.getOngoingJobs();
            jobs.forEach(e -> result.add(new WorkInProgress(e)));
            LOGGER.info("Printing Ongoing jobs:");
            jobs.forEach(LOGGER::info);
            isOngoingJobsInitialized = true;
            return result;
        }
        return workInProgress;
    }
}
