package com.idealista.scraper.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Collections;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public final class FileUtils
{
    private static final Logger LOGGER = LogManager.getLogger(FileUtils.class);

    public static void createFileIfNotExists(String fileName)
    {
        File f = new File(fileName);
        if (!f.exists())
        {
            File parentFile = f.getParentFile();
            if (parentFile != null)
            {
                parentFile.mkdirs();
            }
            try
            {
                f.createNewFile();
            }
            catch (IOException e)
            {
                LOGGER.error("Failed to create a new file: {}, {}", fileName, e.getMessage());
            }
            LOGGER.info("Created a new file: {}", fileName);
        }
    }

    public static Set<String> readFileToLines(String fileName)
    {
        try (Scanner scanner = new Scanner(new File(fileName)))
        {
            return readLinesFromScanner(scanner);
        }
        catch (FileNotFoundException e)
        {
            LOGGER.error("Error while reading the file: {}, error: {}", fileName, e);
            return Collections.emptySet();
        }
    }

    public static Set<String> readStringToLines(String source)
    {
        try (Scanner scanner = new Scanner(source))
        {
            return readLinesFromScanner(scanner);
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

    private static Set<String> readLinesFromScanner(Scanner scanner)
    {
        Set<String> lines = new HashSet<>();
        while (scanner.hasNextLine())
        {
            lines.add(scanner.nextLine().trim());
        }
        LOGGER.info("Successfully read the source. Total lines read: {}", lines.size());
        return lines;
    }
}
