package com.cactusglobal.whiteboard.model;

import java.time.LocalDateTime;
import java.util.Objects;

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

    @Override
    public boolean equals(Object obj)
    {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Job other = (Job) obj;
        return Objects.equals(code, other.code) && units == other.units;
    }

    public String getCode()
    {
        return code;
    }

    public LocalDateTime getDeadline()
    {
        return deadline;
    }

    public int getUnits()
    {
        return units;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(code, units);
    }

    public void setCode(String code)
    {
        this.code = code;
    }

    public void setDeadline(LocalDateTime deadline)
    {
        this.deadline = deadline;
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
