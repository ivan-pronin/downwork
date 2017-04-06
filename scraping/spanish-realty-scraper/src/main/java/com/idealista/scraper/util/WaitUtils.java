package com.idealista.scraper.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitUtils
{
    private static final Logger LOGGER = LogManager.getLogger(WaitUtils.class);

    public static void sleepSeconds(Object object, long seconds)
    {
        sleep(object, seconds * 1000);
    }

    public static void sleep(Object object, long millis)
    {
        synchronized (object)
        {
            try
            {
                LOGGER.debug("{} is sleeping for {} seconds", object, (double) millis / 1000);
                object.wait(millis);
            }
            catch (InterruptedException e)
            {
                LOGGER.error("Error while sleeping for object: {}", object);
            }
        }
    }
}
