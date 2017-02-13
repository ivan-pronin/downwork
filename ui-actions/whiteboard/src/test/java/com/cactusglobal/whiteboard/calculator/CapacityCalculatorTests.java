package com.cactusglobal.whiteboard.calculator;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;

import com.cactusglobal.whiteboard.model.Job;
import com.cactusglobal.whiteboard.model.WorkInProgress;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CapacityCalculatorTests
{
    private static final int DAILY_WORKLOAD = 2660;
    private LocalDateTime startTimeYesterday;
    private LocalDateTime deadlineTimeThreeDays;
    private CapacityCalculator capacityCalculator;

    @Before
    public void before()
    {
        capacityCalculator = new CapacityCalculator(DAILY_WORKLOAD);
        startTimeYesterday = LocalDateTime.now().minusDays(1);
        deadlineTimeThreeDays = LocalDateTime.now().plusDays(3);
    }

    @Test
    public void testNoRunningJobsCanNotTakeNewJob()
    {
        capacityCalculator.setDailyWorkloadCapacity(DAILY_WORKLOAD);
        Job job = new Job("job1", deadlineTimeThreeDays, DAILY_WORKLOAD * 4);
        Assert.assertFalse(capacityCalculator.canTakeJob(job));
    }

    @Test
    public void testNoRunningJobsCanTakeNewJob()
    {
        capacityCalculator.setDailyWorkloadCapacity(DAILY_WORKLOAD);
        Job job = new Job("job1", deadlineTimeThreeDays.plusHours(1), DAILY_WORKLOAD * 3);
        Assert.assertTrue(capacityCalculator.canTakeJob(job));
    }

    @Test
    public void testOneRunningJobNotFullCapacityCanNotTakeNew()
    {
        capacityCalculator.setDailyWorkloadCapacity(DAILY_WORKLOAD);
        Job jobInProgress = new Job("job1", deadlineTimeThreeDays, DAILY_WORKLOAD * 2);
        WorkInProgress workInProgress = new WorkInProgress(startTimeYesterday, jobInProgress);
        capacityCalculator.setWorkInProgress(new HashSet<>(Arrays.asList(workInProgress)));
        Job newJob = new Job("job2", deadlineTimeThreeDays.minusHours(2), DAILY_WORKLOAD * 2);
        Assert.assertFalse(capacityCalculator.canTakeJob(newJob));
    }

    @Test
    public void testOneRunningJobNotFullCapacityCanTakeNew()
    {
        capacityCalculator.setDailyWorkloadCapacity(DAILY_WORKLOAD);
        Job jobInProgress = new Job("job1", deadlineTimeThreeDays, DAILY_WORKLOAD * 2);
        WorkInProgress workInProgress = new WorkInProgress(startTimeYesterday, jobInProgress);
        capacityCalculator.setWorkInProgress(new HashSet<>(Arrays.asList(workInProgress)));
        Job newJob = new Job("job2", deadlineTimeThreeDays.plusHours(1), DAILY_WORKLOAD * 2);
        Assert.assertTrue(capacityCalculator.canTakeJob(newJob));
    }

    @Test
    public void testTwoWorkInProgressCanTakeNew()
    {
        capacityCalculator.setDailyWorkloadCapacity(DAILY_WORKLOAD);
        Job jobInProgress1 = new Job("job1", deadlineTimeThreeDays, DAILY_WORKLOAD);
        WorkInProgress workInProgress1 = new WorkInProgress(startTimeYesterday, jobInProgress1);
        Job jobInProgress2 = new Job("job1", deadlineTimeThreeDays, DAILY_WORKLOAD);
        WorkInProgress workInProgress2 = new WorkInProgress(startTimeYesterday.plusHours(10), jobInProgress2);
        capacityCalculator.setWorkInProgress(new HashSet<>(Arrays.asList(workInProgress1, workInProgress2)));
        Job newJob = new Job("job2", deadlineTimeThreeDays.minusHours(2), DAILY_WORKLOAD * 2);
        Assert.assertTrue(capacityCalculator.canTakeJob(newJob));
    }
}
