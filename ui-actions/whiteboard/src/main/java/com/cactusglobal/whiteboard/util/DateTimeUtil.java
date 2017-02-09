package com.cactusglobal.whiteboard.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public final class DateTimeUtil
{
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
            text = text.split(", ")[1];
            try
            {
                return LocalDateTime.parse(text.trim(), DateTimeFormatter.ofPattern("uuuu-MM-dd HH:mm"));
            }
            catch (DateTimeParseException e1)
            {
                System.out.println("Error while converting string to dateTime format: " + text);
                return null;
            }
        }
    }
}
