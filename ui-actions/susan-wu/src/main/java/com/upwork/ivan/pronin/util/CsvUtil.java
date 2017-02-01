package com.upwork.ivan.pronin.util;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

public final class CsvUtil
{
    private String csvFileLocation;

    public CsvUtil(String csvFileLocation)
    {
        this.csvFileLocation = csvFileLocation;
    }

    public List<CSVRecord> readCsvFile()
    {
        try (Reader in = new FileReader(csvFileLocation))
        {
            return CSVFormat.EXCEL.withFirstRecordAsHeader().parse(in).getRecords();
        }
        catch (IOException e)
        {
            System.out.println("Error while reading csv file: " + e.getMessage());
        }
        return null;
    }

    public enum CsvColumn
    {
        STORE_NAME, ACCOUNT_NUMBER, PIN, COST_PRICE, PRICE;
    }
}
