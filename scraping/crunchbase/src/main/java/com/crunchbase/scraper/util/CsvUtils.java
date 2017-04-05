package com.crunchbase.scraper.util;

import com.crunchbase.scraper.model.Company;
import com.crunchbase.scraper.model.HtmlData;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class CsvUtils
{
    private static final String FIRM_NAME = "firm_name";
    private static final String[] FILE_HEADER_MAPPING = {"OriginalOrder", FIRM_NAME, "firm_name_id", "firm_id_linkedin",
            "file1", "file2", "file3", "file4", "file5"};
    private static final CSVFormat csvFileFormat = CSVFormat.RFC4180.withHeader(FILE_HEADER_MAPPING);
    private static final Logger LOGGER = LogManager.getLogger(CsvUtils.class);

    public static Set<String> readCsvToString(String csvFileName, int readStartRow, int readEndRow)
    {
        Set<String> results = new LinkedHashSet<>();
        List<CSVRecord> records = readCsvFile(csvFileName);
        for (int i = readStartRow; i <= readEndRow; i++)
        {
            results.add(records.get(i).get(FIRM_NAME));
        }
        return results;
    }

    public static void updateInputFile(Set<Company> companies, String originalFileName, String updatedFileName)
    {
        List<CSVRecord> records = readCsvFile(originalFileName);
        List<Map<String, String>> results = new LinkedList<>();

        for (Company company : companies)
        {
            CSVRecord record = findRecord(company.getTitle(), records);
            if (record != null)
            {
                Map<String, String> row = new LinkedHashMap<>();
                row.putAll(record.toMap());
                int index = 4;
                for (HtmlData data : company.getHtmlData())
                {
                    row.put("file" + index, data.getFileName());
                    index++;
                }
                results.add(row);
            }
        }

        try (Writer fileWriter = new FileWriter(new File(updatedFileName));
                CSVPrinter printer = new CSVPrinter(fileWriter, csvFileFormat);)
        {
            List<List<String>> data = new LinkedList<>();
            for (Map<String, String> m : results)
            {
                List<String> row = new LinkedList<>();
                for (String value : m.values())
                {
                    row.add(value);
                }
                data.add(row);
            }
            printer.printRecords(data);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing CSV file: {}", e);
        }
    }

    private static CSVRecord findRecord(String title, List<CSVRecord> records)
    {
        for (CSVRecord record : records)
        {
            if (record.get(FIRM_NAME).equals(title))
            {
                return record;
            }
        }
        return null;
    }

    private static List<CSVRecord> readCsvFile(String csvFileName)
    {
        try (FileReader fileReader = new FileReader(new File(csvFileName)))
        {
            return csvFileFormat.parse(fileReader).getRecords();
        }
        catch (IOException e)
        {
            LOGGER.error("Error while reading CSV file: {}", e);
            return Collections.emptyList();
        }
    }
}
