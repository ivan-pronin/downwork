package com.cactusglobal.whiteboard.util;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FileUtils
{
    private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);
    private static final String RETURN = "\n";

    public static void writeTextToFile(String text)
    {
        LOGGER.info("Appending HTML code at file ...");
        String header = RETURN + "======== Appending info ========" + RETURN + DateTimeUtil.getTimeStamp() + RETURN
                + text;
        try
        {
            Files.write(FileSystems.getDefault().getPath("logs", "html_src.txt"), header.getBytes(),
                    StandardOpenOption.CREATE, StandardOpenOption.APPEND);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing to file {}", e);
        }
    }
}
