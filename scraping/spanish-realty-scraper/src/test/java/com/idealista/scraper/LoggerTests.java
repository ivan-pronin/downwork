package com.idealista.scraper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class LoggerTests
{
    // @Test
    public void testJUL() throws Exception
    {
        System.setProperty("java.util.logging.manager", "org.apache.logging.log4j.jul.LogManager");
        System.setProperty("org.apache.logging.log4j.simplelog.level", "system.out");
        Logger LOGGER = LogManager.getLogger(LoggerTests.class);
        LOGGER.info("Driver started");
        LOGGER.error("This is error message");
        System.out.println(" SYS OUT message");
        System.err.println(" SYS ERR message");
    }

    // @Test
    public void testLogger() throws Exception
    {
        Logger LOGGER = LogManager.getLogger(LoggerTests.class);
        synchronized (this)
        {
            for (int i = 0; i < 30; i++)
            {
                LOGGER.info("Wait BEGIN");
                this.wait(1000);
                LOGGER.info("Wait Ended: counter {}", i);
            }
        }
    }

    //@Test
    public void testLoggerFormatting() throws Exception
    {
        Instant start = Instant.now();
        Logger LOGGER = LogManager.getLogger(LoggerTests.class);
        LOGGER.info(" message INFO bla-ba=l");
        LOGGER.error(" message INFO bla-ba=l");
        new Thread(() ->
        {
            Logger LOGGER2 = LogManager.getLogger();
            LOGGER2.info("THreaded logger infi");
            LOGGER2.error("THreaded logger error");
        }).start();
        Duration d = Duration.between(start, start.plus(2, ChronoUnit.HOURS).plus(5, ChronoUnit.MINUTES).plusSeconds(45));
        LOGGER.info("Total time taken: {} hrs {} mins {} sec", d.toHours(), d.toMinutes() % 60, d.getSeconds() % 60);
    }
    
    @Test
    public void testPrintEnvironmentInfo() throws Exception
    {
        System.getenv().forEach( (k,v) -> System.out.println(k + " = " + v));
        System.out.println(" ++++++++++++++++++++++");
        System.out.println(" ++++++++++++++++++++++");
        System.getProperties().forEach( (k,v) -> System.out.println(k + " = " + v));
    }

}
