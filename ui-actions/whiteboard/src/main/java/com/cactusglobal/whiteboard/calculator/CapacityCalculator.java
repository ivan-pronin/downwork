package com.cactusglobal.whiteboard.calculator;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

import com.cactusglobal.whiteboard.model.Job;
import com.cactusglobal.whiteboard.model.WorkInProgress;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CapacityCalculator
{
    private static final Logger LOGGER = LogManager.getLogger(CapacityCalculator.class);

    private Set<WorkInProgress> workInProgress = new HashSet<>();
    private int dailyWorkloadCapacity;

    public CapacityCalculator(int dailyWorkloadCapacity)
    {
        this.dailyWorkloadCapacity = dailyWorkloadCapacity;
    }

    public boolean canTakeJob(Job newJob)
    {
        LOGGER.info("Deciding whether the job can be accepted...");
        Set<WorkInProgress> copyWorkInProgress = new HashSet<>(workInProgress);
        double workRateOfRemainingWork = 0;
        LOGGER.info("Printing ongoing Work objects >>");
        workInProgress.forEach(LOGGER::info);
        for (WorkInProgress work : copyWorkInProgress)
        {
            LocalDateTime startNow = LocalDateTime.now();
            double completedUnits = (getWorkHours(work) / copyWorkInProgress.size()) * getHourlyCapacity();
            double initialUnits = work.getJob().getUnits();
            double remainingUnits = initialUnits - completedUnits;
            workRateOfRemainingWork = +getRateOfWork(remainingUnits, startNow, work.getJob().getDeadline());
        }
        LOGGER.info("Work Rate of remaining Work :{}", workRateOfRemainingWork);
        WorkInProgress tempWork = new WorkInProgress(LocalDateTime.now(), newJob);
        double rateOrWork = getRateOrWork(tempWork, tempWork.getStartTime());
        LOGGER.info("Work Rate of new work: {}", rateOrWork);
        double compare = (1 - workRateOfRemainingWork) - rateOrWork;
        LOGGER.info("Comparison of work rates: {}", compare);
        boolean canAcceptJob = compare > -0.02;
        LOGGER.info("Can accept the job: {}", canAcceptJob);
        return canAcceptJob;
    }

    public double getRateOfWork(double unitsToDo, LocalDateTime startTime, LocalDateTime deadline)
    {
        double hoursToCompleteWork = Duration.between(startTime, deadline).toHours();
        double workRate = unitsToDo / hoursToCompleteWork;
        return workRate / getHourlyCapacity();
    }

    public double getRateOrWork(WorkInProgress work, LocalDateTime startTime)
    {
        Job job = work.getJob();
        return getRateOfWork(job.getUnits(), startTime, job.getDeadline());
    }

    public void setDailyWorkloadCapacity(int dailyWorkloadCapacity)
    {
        this.dailyWorkloadCapacity = dailyWorkloadCapacity;
    }

    public void setWorkInProgress(Set<WorkInProgress> workInProgress)
    {
        this.workInProgress = workInProgress;
    }

    private double getHourlyCapacity()
    {
        return dailyWorkloadCapacity / 24;
    }

    private double getWorkHours(WorkInProgress work)
    {
        LocalDateTime startTime = work.getStartTime();
        LocalDateTime endTime = LocalDateTime.now();
        return Duration.between(startTime, endTime).toHours();
    }
}
