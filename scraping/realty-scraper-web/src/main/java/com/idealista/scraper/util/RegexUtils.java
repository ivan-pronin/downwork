package com.idealista.scraper.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class RegexUtils
{
    private static final Logger LOGGER = LogManager.getLogger(RegexUtils.class);

    public static int extractBigNumber(String text)
    {
        if (text == null)
        {
            return -1;
        }
        text = replaceAllNonDigitCharacters(text);
        Pattern pattern = Pattern.compile("(\\d*)");
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return Integer.parseInt(m.group(1));
        }
        return -1;
    }

    public static int extractDigit(String text)
    {
        return (int) executeExpression(text, "(\\d{1})");
    }

    public static long executeExpression(String text, String expression)
    {
        if (text == null)
        {
            LOGGER.debug("ExtractDigit: input string is null");
            return -1;
        }
        Pattern pattern = Pattern.compile(expression);
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return Long.parseLong(m.group());
        }
        return -1;
    }

    public static int extractNumber(String text)
    {
        return (int) executeExpression(text, "(\\d+)");
    }

    public static String extractPostalCode(String text)
    {
        int result = (int) executeExpression(text, "(\\d{5,})");
        return result == -1 ? null : "" + result;
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

    public static String getDigitStringOccurenceInText(String string, String text)
    {
        if (text == null || string == null)
        {
            return null;
        }
        Pattern pattern = Pattern.compile("\\d(.?|.?\\n)" + string);
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return m.group(0);
        }
        return null;
    }

    public static String replaceAllNonDigitCharacters(String text)
    {
        return text.replaceAll("[^0-9]+", "");
    }

    private static String getSecondArrayElement(String[] parts)
    {
        if (parts.length > 1)
        {
            return parts[1].trim();
        }
        return null;
    }

    public static String extractTextBetweenParenthesis(String text)
    {
        if (text == null)
        {
            return null;
        }
        Pattern pattern = Pattern.compile("\\((.*?)\\)");
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            return m.group(1);
        }
        return null;
    }
}
