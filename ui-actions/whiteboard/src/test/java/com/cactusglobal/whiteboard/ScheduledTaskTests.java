package com.cactusglobal.whiteboard;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import com.cactusglobal.whiteboard.model.Job;
import com.cactusglobal.whiteboard.model.WorkInProgress;
import com.cactusglobal.whiteboard.ui.DashboardPageProcessor;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Assert;
import org.junit.Test;

public class ScheduledTaskTests
{
    private class TestReturningTask extends TimerTask
    {

        @Override
        public void run()
        {
            System.out.println("Running...");
            int dice = new Random().nextInt(3);
            if (dice == 0)
            {
                System.out.println("returning!!");
                return;
            }
        }

    }

    @Test
    public void testReturningTask() throws InterruptedException
    {
        TestReturningTask task = new TestReturningTask();
        Timer timer = new Timer();
        int timeoutSeconds = 1;
        Thread mainThread = Thread.currentThread();
        timer.schedule(task, 0, timeoutSeconds * 1000);
        synchronized (mainThread)
        {
            mainThread.wait(timeoutSeconds * 1000 * 10);
        }
    }

    private class TestTask extends TimerTask
    {
        private Set<WorkInProgress> workInProgress = new HashSet<>();
        private long startTime;

        @Override
        public void run()
        {
            workInProgress = initWorkInProgress();
            long currentTime = System.currentTimeMillis();
            if ((currentTime - startTime) > 1000)
            {
                {
                    workInProgress.add(new WorkInProgress(new Job("code1", LocalDateTime.now(), 1000)));
                    System.out.println("Total jobs: ");
                    System.out.println(workInProgress);
                }
            }
        }

        private Set<WorkInProgress> initWorkInProgress()
        {
            if (workInProgress.isEmpty())
            {
                System.out.println("Initializing workInProgress list ... ");
                Set<WorkInProgress> result = new HashSet<>();
                Document document = null;
                try
                {
                    document = Jsoup.parse(new File("ongoingJobs.txt"), "utf-8");
                }
                catch (IOException e1)
                {
                    e1.printStackTrace();
                }
                DashboardPageProcessor dashboardPageProcessor = new DashboardPageProcessor(document);
                List<Job> jobs = dashboardPageProcessor.getOngoingJobs();
                jobs.forEach(e -> result.add(new WorkInProgress(e)));
                System.out.println("Printing Ongoing jobs:");
                jobs.forEach(e -> System.out.println(e));
                return result;
            }
            System.out.println("No need to initizlize");
            return workInProgress;
        }

        private void setStartTime(long startTime)
        {
            this.startTime = startTime;
        }
    }

    //@Test
    public void testGetJobs()
    {
        Document doc;
        try
        {
            doc = Jsoup.parse(new File("jobs.txt"), "utf-8");
            DashboardPageProcessor dashboardPageProcessor = new DashboardPageProcessor(doc);
            System.out.println(dashboardPageProcessor.getAllocatedJobs());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //@Test
    public void testGetOngoingJObs() throws Exception
    {
        Document doc;
        try
        {
            doc = Jsoup.parse(new File("ongoingJobs.txt"), "utf-8");
            DashboardPageProcessor dashboardPageProcessor = new DashboardPageProcessor(doc);
            System.out.println(dashboardPageProcessor.getOngoingJobs());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    //@Test
    public void testTimerThreadAddNewWork() throws Exception
    {
        TestTask task = new TestTask();
        task.setStartTime(System.currentTimeMillis());
        Timer timer = new Timer();
        int timeoutSeconds = 1;
        Thread mainThread = Thread.currentThread();
        timer.schedule(task, 0, timeoutSeconds * 1000);
        synchronized (mainThread)
        {
            mainThread.wait(timeoutSeconds * 1000 * 3);
        }
        System.out.println("Total jobs: " + task.workInProgress);
        Assert.assertEquals(2, task.workInProgress.size());
    }

    private String currentThreadName()
    {
        return Thread.currentThread().getName();
    }
}
