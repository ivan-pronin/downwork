package com.idealista.scraper;

import static org.junit.Assert.*;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.idealista.scraper.util.RegexUtils;

import org.junit.Test;

public class RegexTests
{
    @Test
    public void testName() throws Exception
    {
        String x = "Hello (Java)";
        Matcher m = Pattern.compile("\\((.*?)\\)").matcher(x);
        while (m.find())
        {
            System.out.println(m.group(1));
        }
    }
    
    @Test
    public void testName2() throws Exception
    {
        String text = "Centro (Boadilla del Monte)";
        System.out.println(RegexUtils.extractTextBetweenParenthesis(text));
    }
    
    @Test
    public void testName3() throws Exception
    {
      String s1 = "City1";
      String s2 = "City2 (District)";
      System.out.println(s1.split("\\(")[0]);
      System.out.println(s2.split("\\(")[0]);
    }
}
