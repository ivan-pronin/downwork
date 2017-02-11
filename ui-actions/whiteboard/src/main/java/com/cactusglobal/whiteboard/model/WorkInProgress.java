package com.cactusglobal.whiteboard.model;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public int hashCode()
    {
        return Objects.hash(job);
    }

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        WorkInProgress other = (WorkInProgress) obj;
        return Objects.equals(job, other.job);
    }

    @Override
    public String toString()
    {
        return "WorkInProgress [startTime=" + startTime + ", job=" + job + "]";
    }
}
