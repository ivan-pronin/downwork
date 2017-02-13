package com.cactusglobal.whiteboard.util;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class DateTimeUtil
{
    private static final Logger LOGGER = LogManager.getLogger(DateTimeUtil.class);

    public static String getTimeStamp()
    {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd_HH-mm-ss_SSS");
        return sdf.format(new Timestamp(new java.util.Date().getTime()));
    }

    public static LocalDateTime parseFromGMTString(String text)
    {
        try
        {
            String copy = text;
            if (!copy.endsWith("GMT"))
            {
                copy = copy.trim() + " GMT";
            }
            return LocalDateTime.parse(copy.trim(), DateTimeFormatter.RFC_1123_DATE_TIME);
        }
        catch (DateTimeParseException e)
        {
            String[] parts = text.split(", ");
            if (parts.length > 1)
            {
                text = parts[1];
            }
            else
            {
                LOGGER.error("Could not split DateTime string {}", text);
                return null;
            }
            try
            {
                return LocalDateTime.parse(text.trim(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"));
            }
            catch (DateTimeParseException e1)
            {
                LOGGER.error("Error while converting string to dateTime format: {}", text);
                return null;
            }
        }
    }
}
