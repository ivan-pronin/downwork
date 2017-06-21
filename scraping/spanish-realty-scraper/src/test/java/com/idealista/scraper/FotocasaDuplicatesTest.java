package com.idealista.scraper;

import com.idealista.scraper.util.URLUtils;

import org.junit.Test;

import java.net.URL;

public class FotocasaDuplicatesTest
{
    @Test
    public void testName() throws Exception
    {
        String s1 = "http://www.fotocasa.es/vivienda/madrid-capital/patio-valdeacederas-142497909?RowGrid=3&tti=1&opi=300";
        String s2 = "http://www.fotocasa.es/vivienda/madrid-capital/patio-valdeacederas-142497909?RowGrid=16&tti=1&opi=300";
        long id1 = URLUtils.extractIdFromUrl(new URL(s1));
        long id2 = URLUtils.extractIdFromUrl(new URL(s2));
        System.out.println("id1: " + id1);
        System.out.println("id2: " + id2);
        System.out.println(Long.compare(id1, id2));
    }
}
