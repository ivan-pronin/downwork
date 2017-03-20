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
    
    //@Test
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
    
    //@Test
    public void testExtractNumber()
    {
        String s1 = "ass 1 m2 asf2";
        String s2 = "ass 22 m2  asf2";
        String s3 = "ass 333 m2 asf2";
        System.out.println("" + RegexUtils.extractBigNumber(s1));
        System.out.println("" + RegexUtils.extractNumber(s1));
        
        System.out.println("" + RegexUtils.extractBigNumber(s2));
        System.out.println("" + RegexUtils.extractNumber(s2));
        
        System.out.println("" + RegexUtils.extractBigNumber(s3));
        System.out.println("" + RegexUtils.extractNumber(s3));
    }
    
    @Test
    public void testGetTags() throws Exception
    {
        final String string = "habitaciones";
        final String text = "Rozas De Madrid (las), Las Rozas de Madrid - Las Matas  - Peñascales, 450 m2, 5 \n"
             + "habitaciones, 4 baños, 1 aseos, casi nuevo, aa, armarios, calefacción, garaje,\n"
             + "jardín, terraza, trastero, CocinaOffice, Piscina, suite, Amueblado,\n"
             + "Electrodomésticos, Horno, Lavadora, Microondas, Nevera, mascotas, videoportero,\n"
             + "puerta blindada, lavadero, 2900 eur. / mes, Magnifico chalet independiente en los\n"
             + "Peñascales de 450m sobre terreno de 1000m. Amplios espacios abiertos, muy\n"
             + "luminoso, primeras calidades. Disribuido en tres plantas + sotano/garaje. ";
        String foundMatch = RegexUtils.getDigitStringOccurenceInText(string, text);
        System.out.println(foundMatch);
        System.out.println("Digit: " + Integer.parseInt(RegexUtils.extractDigit(foundMatch)));
    }
}
