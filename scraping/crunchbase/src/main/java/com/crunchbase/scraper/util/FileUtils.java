package com.crunchbase.scraper.util;

import com.crunchbase.scraper.model.HtmlData;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
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
        while (sc.hasNextLine())
        {
            urlsString.add(sc.nextLine());
            continue;
        }
        return urlsString.stream().map(t ->
        {
            try
            {
                return new URL(t);
            }
            catch (MalformedURLException e)
            {
                System.out.println("Error while parsing URL for: " + t);
                e.printStackTrace();
            }
            return null;
        }).collect(Collectors.toSet());
    }

    public static void saveHtmlToFile(HtmlData data, int counter, String title, String doc, String targetFile)
    {
        String nameAppender = counter == 1 ? "" : "_" + counter;
        String fileName = title + nameAppender + ".html";
        try
        {
            String subFolder = targetFile.split("\\.")[0];
            String htmlFileName = "./htmlFiles/" + subFolder + "/" + fileName;
            org.apache.commons.io.FileUtils.writeStringToFile(new File(htmlFileName),
                    doc, Charset.defaultCharset());
            data.setFileName(fileName);
            LOGGER.info("Saved new HTML file: {}", htmlFileName);
        }
        catch (IOException e1)
        {
            LOGGER.error("Failed to write to file: {}", e1.getMessage());
        }
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
