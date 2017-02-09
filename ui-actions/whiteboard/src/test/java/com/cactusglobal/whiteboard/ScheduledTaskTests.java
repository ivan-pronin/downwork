package com.cactusglobal.whiteboard;

import com.cactusglobal.whiteboard.ScheduledTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class ScheduledTaskTests
{

    @Test
    public void testGetJobs()
    {
        Document doc;
        try
        {
            doc = Jsoup.parse(new File("jobs.txt"), "utf-8");
            ScheduledTask task = new ScheduledTask();
            System.out.println(task.getAllocatedJobs(doc));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    @Test
    public void testGetOngoingJObs() throws Exception
    {
        Document doc;
        try
        {
            doc = Jsoup.parse(new File("ongoingJobs.txt"), "utf-8");
            ScheduledTask task = new ScheduledTask();
            System.out.println(task.getOngoingJobs(doc));
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
