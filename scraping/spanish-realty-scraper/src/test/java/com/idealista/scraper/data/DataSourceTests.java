package com.idealista.scraper.data;

import org.junit.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class DataSourceTests
{
    // @Test
    public void testUpdateFile() throws Exception
    {
        try (FileWriter writer = new FileWriter("write.txt", true))
        {
            writer.write("a 1\n");
            writer.write("bb 22 \n");
        }
        catch (IOException e)
        {

        }
    }

    @Test
    public void testName() throws Exception
    {
        String fileName = "./data/file.txt";
        File f = new File(fileName);
        if (!f.exists())
        {
            File parentFile = f.getParentFile();
            if (parentFile != null)
            {
                parentFile.mkdirs();
            }
            f.createNewFile();
            System.out.println("File created");
        }
    }
}
