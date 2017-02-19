package com.idealista.scraper.util;

import static org.junit.Assert.*;

import org.junit.Test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RegexUtilsTests
{
    @Test
    public void testExtractDigit() throws Exception
    {
        String text =  "1 bedroom ";
        System.out.println(RegexUtils.extractDigit(text));
    }
    
    @Test
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
}
