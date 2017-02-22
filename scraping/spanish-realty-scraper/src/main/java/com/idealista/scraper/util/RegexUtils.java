package com.idealista.scraper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class RegexUtils
{
    public static String extractPostalCode(String text)
    {
        if (text == null)
        {
            return null;
        }
        Pattern pattern = Pattern.compile("(\\d{5,})");
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return m.group(1);
        }
        return null;
    }

    public static String extractDigit(String text)
    {
        if (text == null)
        {
            return null;
        }
        Pattern pattern = Pattern.compile("(\\d{1})");
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return m.group(1);
        }
        return null;
    }

    public static int extractNumber(String text)
    {
        if (text == null)
        {
            return -1;
        }
        Pattern pattern = Pattern.compile("(\\d+)");
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return Integer.parseInt(m.group(1));
        }
        return -1;
    }

    public static int extractBigNumber(String text)
    {
        if (text == null)
        {
            return -1;
        }
        text = text.replaceAll("[^-?0-9]+", "");
        Pattern pattern = Pattern.compile("(\\d*)");
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return Integer.parseInt(m.group(1));
        }
        return -1;
    }

    public static String extractTextAfterAnchor(String text, String anchor)
    {
        if (text == null)
        {
            return null;
        }
        String[] parts = text.split(anchor);
        return getSecondArrayElement(parts);
    }

    public static String extractTextBetweenTwoNumbers(String text)
    {
        if (text == null)
        {
            return null;
        }
        String[] parts = text.split("\\d+");
        return getSecondArrayElement(parts);
    }

    private static String getSecondArrayElement(String[] parts)
    {
        if (parts.length > 1)
        {
            return parts[1].trim();
        }
        return null;
    }
}
