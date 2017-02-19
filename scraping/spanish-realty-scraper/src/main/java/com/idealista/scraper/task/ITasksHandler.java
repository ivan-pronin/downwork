package com.idealista.scraper.task;

import java.util.Queue;

public interface ITasksHandler
{
    void waitTasksForCompletion(Queue<?> tasks);
}
