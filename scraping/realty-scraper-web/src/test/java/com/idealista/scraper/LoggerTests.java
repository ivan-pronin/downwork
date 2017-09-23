package com.idealista.scraper;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

public class LoggerTests
{

    private static final Logger LOGGER = LogManager.getLogger(LoggerTests.class);

    @Test
    public void testName() throws Exception
    {
        List<Double> series = new ArrayList<>();
        series.add(1.1);// Populated with, e.g., time series data
        series.add(2.4);
        series.add(3.6);
        series.add(0.2);

        List<Double> seriesChanges = new ArrayList<>();
        IntStream.range(1, series.size()).forEach(e -> seriesChanges.add(Math.abs(series.get(e - 1) - series.get(e))));
        System.out.println(seriesChanges);

        List<Double> seriesChanges2 = new ArrayList<>();
        for (int i = 1; i < series.size(); i++)
        {
            seriesChanges2.add(Math.abs(series.get(i - 1) - series.get(i)));
        }
        System.out.println(seriesChanges2);
        System.out.println(Arrays.toString(IntStream.of(1, series.size() - 1).toArray()));
    }

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

    // @Test
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
        Duration d = Duration.between(start,
                start.plus(2, ChronoUnit.HOURS).plus(5, ChronoUnit.MINUTES).plusSeconds(45));
        LOGGER.info("Total time taken: {} hrs {} mins {} sec", d.toHours(), d.toMinutes() % 60, d.getSeconds() % 60);
    }

    // @Test
    public void testPrintEnvironmentInfo() throws Exception
    {
        System.getenv().forEach((k, v) -> System.out.println(k + " = " + v));
        System.out.println(" ++++++++++++++++++++++");
        System.out.println(" ++++++++++++++++++++++");
        System.getProperties().forEach((k, v) -> System.out.println(k + " = " + v));
    }

}
