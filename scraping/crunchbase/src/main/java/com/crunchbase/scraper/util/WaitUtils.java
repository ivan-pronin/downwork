package com.crunchbase.scraper.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class WaitUtils
{
    private static final Logger LOGGER = LogManager.getLogger(WaitUtils.class);

    public static void sleep(Object object, long millis)
    {
        synchronized (object)
        {
            try
            {
                object.wait(millis);
            }
            catch (InterruptedException e)
            {
                LOGGER.error("Error while sleeping for object: {}", object);
            }
        }
    }
}
