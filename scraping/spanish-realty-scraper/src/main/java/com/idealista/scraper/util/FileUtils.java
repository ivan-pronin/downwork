package com.idealista.scraper.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public final class FileUtils
{
    private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

    public static Set<String> readFileToLines(String fileName)
    {
        try (Scanner scanner = new Scanner(new File(fileName)))
        {
            Set<String> lines = new HashSet<>();
            while (scanner.hasNextLine())
            {
                lines.add(scanner.nextLine());
            }
            LOGGER.info("Successfully read the file: {}. Total lines read: {}", fileName, lines.size());
            return lines;
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("Error while reading the file: {}, error: {}", fileName, e);
            return Collections.emptySet();
        }
    }

    public static Set<URL> readUrlsFromFile(String fileName)
    {
        Scanner sc = null;
        try
        {
            sc = new Scanner(new File(fileName));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        Set<String> urlsString = new HashSet<>();
        String separator = "https://";
        while (sc.hasNextLine())
        {
            String line = sc.nextLine();
            if (line.contains(separator))
            {
                urlsString.add(line.split(separator)[1]);
            }
            continue;
        }
        return urlsString.stream().map(t ->
        {
            try
            {
                return new URL(separator + t);
            }
            catch (MalformedURLException e)
            {
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet());
    }
    
    public static void exportToExcel(List<Map<String, String>> data, String fileName)
    {
        File file = new File(fileName);
        try (FileWriter writer = new FileWriter(file))
        {
            for (Map<String, String> row : data)
            {
                for (Entry<String, String> entry : row.entrySet())
                {
                    writer.append(entry.getKey() + " = " + entry.getValue() + "\t");
                }
                writer.append("\n");
            }
        }
        catch (IOException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
