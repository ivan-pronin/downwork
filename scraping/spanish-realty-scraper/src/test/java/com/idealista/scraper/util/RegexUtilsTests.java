package com.idealista.scraper.util;

import org.junit.Test;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtilsTests
{
    //@Test
    public void testExtractDigit() throws Exception
    {
        String text =  "1 bedroom ";
        System.out.println(RegexUtils.extractDigit(text));
    }
    
    //@Test
    public void testExtractPrice() throws Exception
    {
        Pattern pattern = Pattern.compile("(\\d*)");
        String text = "1.165,000";
        text = text.replaceAll("[^-?0-9]+", ""); 
        Matcher m = pattern.matcher(text);
        if (m.find())
        {
            System.out.println(m.group(1));
        }
    }
    
    @Test
    public void testExtractTextBetweenTwoNumbers() throws Exception
    {
        Pattern pattern = Pattern.compile("(?<=\\d+)(.*)(?=\\d+)");
        String s2 = "avda. Buenos Aires 54 A Coruña 15004";
        Matcher m = pattern.matcher(s2);
        if (m.find())
        {
            System.out.println(m.group(1));
        }

        String[] parts = s2.split("(\\d+)");
        System.out.println(Arrays.toString(parts));
    }
}
