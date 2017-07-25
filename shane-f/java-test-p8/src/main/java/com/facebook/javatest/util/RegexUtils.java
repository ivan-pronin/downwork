package com.facebook.javatest.util;

import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils
{
    private static final Logger LOGGER = LogManager.getLogger(RegexUtils.class);
    
    private RegexUtils()
    {
    }
    
    public static long extractNumber(String text)
    {
        if (StringUtils.isEmpty(text))
        {
            return -1;
        }
        text = replaceAllNonDigitCharacters(text);
        Pattern pattern = Pattern.compile("(\\d*)");
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return Long.parseLong(m.group(1));
        }
        LOGGER.error("Could not parse number for string {}", text);
        return -1;
    }
    
    public static long extractId(String text)
    {
        if (text == null)
        {
            return -1;
        }
        Pattern pattern = Pattern.compile("(\\d{10,})");
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return Long.parseLong(m.group(1));
        }
        LOGGER.error("Could not parse ID for string {}", text);
        return -1;
    }
    

    private static String replaceAllNonDigitCharacters(String text)
    {
        return text.replaceAll("[^0-9]+", "");
    }
}
