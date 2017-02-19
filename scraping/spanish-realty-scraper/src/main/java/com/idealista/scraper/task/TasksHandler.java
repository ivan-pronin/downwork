package com.idealista.scraper.task;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

/**
 * Utility class to wait for tasks for completion
 * @author Ivan_Pronin
 */
public class TasksHandler implements ITasksHandler
{
    private static final Logger LOGGER = LogManager.getLogger(TasksHandler.class);

    private long taskCompletionTimeout;
    private TimeUnit taskCompletionTimeUnit;

    @Override
    public void waitTasksForCompletion(Queue<?> tasks)
    {
        Future<?> task;
        Queue<?> clonedTasks = new ConcurrentLinkedQueue<>(tasks);
        while ((task = (Future<?>) clonedTasks.poll()) != null)
        {
            if (!task.isDone())
            {
                LOGGER.info("Waiting for task completion");
                try
                {
                    task.get(taskCompletionTimeout, taskCompletionTimeUnit);
                    LOGGER.info("Task is completed");
                }
                catch (TimeoutException e)
                {
                    LOGGER.error("The timeout elapsed. Task is not successfully completed: " + e.getMessage(), e);
                    if (task.cancel(true))
                    {
                        LOGGER.warn("Task has been cancelled");
                    }
                    else
                    {
                        LOGGER.error("Unable to cancel task");
                    }
                    continue;
                }
                catch (ExecutionException e)
                {
                    LOGGER.error("Task excecution error", e);
                }
                catch (InterruptedException e)
                {
                    LOGGER.error("Task was interrupted", e);
                }
            }
        }
        LOGGER.info("All tasks have been finished!");
    }

    public void setTaskCompletionTimeout(long taskCompletionTimeout)
    {
        this.taskCompletionTimeout = taskCompletionTimeout;
    }

    public void setTaskCompletionTimeUnit(TimeUnit taskCompletionTimeUnit)
    {
        this.taskCompletionTimeUnit = taskCompletionTimeUnit;
    }
}
