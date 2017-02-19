package com.idealista.scraper.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

public class FileUtilsTests
{
    @Test
    public void testWriteToFile()
    {
        List<Map<String, String>> data = new ArrayList<>();
        Map<String, String> row1 = new HashMap<>();
        row1.put("Key1", "Value1");
        row1.put("Key2", "Value2");
        Map<String, String> row2 = new HashMap<>();
        row2.put("Key1 222", "Value1 222");
        row2.put("Key2 222", "Value2 222");
        data.add(row1);
        data.add(row2);
        FileUtils.exportToExcel(data, "test.xls");
    }
}
