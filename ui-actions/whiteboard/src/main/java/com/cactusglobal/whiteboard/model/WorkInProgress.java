package com.cactusglobal.whiteboard.model;

import java.time.LocalDateTime;

public class WorkInProgress
{
    private LocalDateTime startTime;
    private Job job;

    public WorkInProgress(LocalDateTime startTime, Job job)
    {
        this.startTime = startTime;
        this.job = job;
    }
    
    public WorkInProgress(Job job)
    {
        startTime = LocalDateTime.now();
        this.job = job;
    }

    public LocalDateTime getStartTime()
    {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime)
    {
        this.startTime = startTime;
    }

    public Job getJob()
    {
        return job;
    }

    public void setJob(Job job)
    {
        this.job = job;
    }
}
