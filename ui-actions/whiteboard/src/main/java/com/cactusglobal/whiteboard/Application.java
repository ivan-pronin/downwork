package com.cactusglobal.whiteboard;

import java.util.Timer;

public class Application
{
    public static void main(String[] args)
    {
        System.out.println("Program started at: " + ScheduledTask.getTimeStamp());
        Timer timer = new Timer();
        ScheduledTask scheduledTask = new ScheduledTask();
        timer.schedule(scheduledTask, 0, 300000); // execute every 5 mins
    }
}
