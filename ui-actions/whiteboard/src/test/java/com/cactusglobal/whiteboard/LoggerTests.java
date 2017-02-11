package com.cactusglobal.whiteboard;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class LoggerTests
{
    private static final Logger LOGGER = LogManager.getLogger(LoggerTests.class);
    
    @Test
    public void testLoggerInit()
    {
        LOGGER.debug("debug");
        LOGGER.info("info: {}");
        LOGGER.error("error: {}", "error text");
    }
    
    //@Test
    public void testError()
    {
        LOGGER.info("Appending HTML code at file ...");
        String text = "some test wo write";
        try
        {
            Files.write(FileSystems.getDefault().getPath("logs1", "log.txt"), text.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing to file {}", e);
        }
    }
}
