package com.cactusglobal.whiteboard;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.junit.Test;

import java.io.IOException;

public final class FClass
{
    @Test
    public void testName() throws Exception
    {
            Document doc = Jsoup.connect("https://www.reddit.com/new/").get();
            Elements certainLinks = doc.select("a[href*=https://www.reddit.com/user/]");
            certainLinks.forEach(l -> System.out.println(l.text()));
    }
}
