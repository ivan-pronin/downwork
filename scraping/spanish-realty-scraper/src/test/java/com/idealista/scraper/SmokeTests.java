package com.idealista.scraper;

import org.junit.Test;

import java.nio.charset.Charset;
import java.util.Arrays;

public class SmokeTests
{
    @Test
    public void testName() throws Exception
    {
        Charset utf8charset = Charset.forName("UTF-8");
        Charset iso88591charset = Charset.forName("ISO-8859-1");
        
        String utf8="Chamartín";
        byte[] utf8bytes = utf8.getBytes("UTF-8");
        byte[] isobytes = utf8.getBytes("ISO-8859-1");
        
        System.out.println("UTF: " + Arrays.toString(utf8bytes));
        System.out.println("ISO: " + Arrays.toString(isobytes));
    }
    
    private void printArrayUnicode(char[] charArray)
    {
        for (char c : charArray)
        {
            printUnicode(c);
        }
    }

    private void printUnicode(char c)
    {
        System.out.println(String.format("\\u%04x", (int)c));
    }
}
