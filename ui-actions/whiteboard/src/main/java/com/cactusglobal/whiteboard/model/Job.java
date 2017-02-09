package com.cactusglobal.whiteboard.model;

import java.time.LocalDateTime;

public class Job
{
    private String code;
    private LocalDateTime deadline;
    private int units;

    public Job(String code, LocalDateTime deadline, int units)
    {
        this.code = code;
        this.deadline = deadline;
        this.units = units;
    }

    public String getCode()
    {
        return code;
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public LocalDateTime getDeadline()
    {
        return deadline;
    }

    public void setDeadline(LocalDateTime deadline)
    {
        this.deadline = deadline;
    }

    public int getUnits()
    {
        return units;
    }

    public void setUnits(int units)
    {
        this.units = units;
    }

    @Override
    public String toString()
    {
        return "Job [code=" + code + ", deadline=" + deadline + ", units=" + units + "]";
    }
}
