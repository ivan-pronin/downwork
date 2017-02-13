package com.cactusglobal.whiteboard;

import org.junit.Test;

public class ArrayTests
{
    @Test
    public void testSplit()
    {
        String text = "abc";
        String[] parts = text.split(" ");
        System.out.println(parts.length);
        System.out.println(parts[0]);
    }
}
