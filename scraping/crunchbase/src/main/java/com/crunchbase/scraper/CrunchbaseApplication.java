package com.crunchbase.scraper;

import com.crunchbase.scraper.service.CrunchbaseScraperService;
import com.crunchbase.scraper.util.DateTimeUtils;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class CrunchbaseApplication
{
    public static void main(String[] args) throws IOException
    {
        AnnotationConfigApplicationContext ctx = new AnnotationConfigApplicationContext(CrunchbaseAppConfig.class);

        // redirectConsoleOutput();

        Runtime runtime = Runtime.getRuntime();
        System.out.println("heapsize is :: " + runtime.totalMemory());
        System.out.println("free memory :: " + runtime.freeMemory());
        System.out.println("max memory :: " + runtime.maxMemory());

        CrunchbaseScraperService service = ctx.getBean(CrunchbaseScraperService.class);
        service.scrap();
        ctx.close();
        //saveConsoleToFile();
    }

    private static void saveConsoleToFile() throws IOException
    {
        String fileName = "./" + DateTimeUtils.getFilenameTimestamp() + "console.log";
        String command = "dir > %s | type %s";
        Runtime.getRuntime().exec(String.format(command, fileName, fileName));
    }

    private static void redirectConsoleOutput()
    {
        try
        {
            String fileName = "./" + DateTimeUtils.getFilenameTimestamp() + "console.log";
            System.setOut(createStream(fileName));
            System.setErr(createStream(fileName));
        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
    }

    private static PrintStream createStream(String fileName) throws FileNotFoundException
    {
        return new PrintStream(new FileOutputStream(fileName));
    }
}
