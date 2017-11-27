package com.idealista.scraper.model;

import java.time.Instant;

import org.springframework.stereotype.Component;

@Component
public class LaunchResult
{
    private String message;
    private Instant startTime;
    private Instant endTime;

    public String getMessage()
    {
        return message;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public Instant getStartTime()
    {
        return startTime;
    }

    public void setStartTime(Instant startTime)
    {
        this.startTime = startTime;
    }

    public Instant getEndTime()
    {
        return endTime;
    }

    public void setEndTime(Instant endTime)
    {
        this.endTime = endTime;
    }
}
