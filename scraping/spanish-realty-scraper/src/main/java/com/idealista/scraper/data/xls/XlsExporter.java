package com.idealista.scraper.data.xls;

import com.idealista.scraper.model.Advertisment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

@Component
public class XlsExporter
{
    private static final String IDEALISTA_DATA = "Idealista_data";
    private static final Logger LOGGER = LogManager.getLogger(XlsExporter.class);
    private Workbook wb;
    private Sheet sheet;
    private String fileName;

    public XlsExporter(@Value(value = "${xlsFileName}") String fileName)
    {
        this.fileName = fileName;
    }

    public void initWorkBook()
    {
        if (!new File(fileName).exists())
        {
            try (FileOutputStream fileOut = new FileOutputStream(fileName, true))
            {
                wb = new HSSFWorkbook();
                sheet = wb.createSheet(IDEALISTA_DATA);
                Row row = sheet.createRow((short) 0);
                createHeader(row);
                wb.write(fileOut);
            }
            catch (IOException e)
            {
                LOGGER.error("Error while creating a workbook: {}", e);
            }
            LOGGER.info("Workbook with name <{}> successfully created", fileName);
        }
        else
        {
            try (FileInputStream fileIn = new FileInputStream(fileName))
            {
                wb = new HSSFWorkbook(fileIn);
                sheet = wb.getSheet(IDEALISTA_DATA);
                LOGGER.info("Workbook with name <{}> already exists, will append new data there", fileName);
            }
            catch (IOException e)
            {
                LOGGER.error("Failed to read a workbook: {}", fileName, e.getMessage());
            }
        }
    }

    public void appendResults(Set<Advertisment> advertisments)
    {
        LOGGER.info("Writing new <{}> advertisments to XLS...", advertisments.size());
        try (FileOutputStream fileOut = new FileOutputStream(fileName))
        {
            int lastRow = sheet.getLastRowNum();
            for (Advertisment ad : advertisments)
            {
                writeAdvertismentToRow(ad, sheet.createRow(++lastRow));
            }
            wb.write(fileOut);
        }
        catch (IOException e)
        {
            LOGGER.error("Error while writing new results to XLS: {}", e);
        }
    }

    private void writeAdvertismentToRow(Advertisment ad, Row row)
    {
        if (ad == null)
        {
            LOGGER.error("Could not write advertisment to XLS - it's NULL");
            return;
        }
        if (row == null)
        {
            LOGGER.error("Could not write advertisment to XLS - row is NULL");
            return;
        }
        int columnIndex = 0;
        row.createCell(columnIndex).setCellValue(ad.getTitle());
        row.createCell(++columnIndex).setCellValue(ad.getType());
        row.createCell(++columnIndex).setCellValue(ad.getSubType() == null ? "" : ad.getSubType().name());
        row.createCell(++columnIndex).setCellValue(ad.getProvince());
        row.createCell(++columnIndex).setCellValue(ad.getDateOfListing());
        row.createCell(++columnIndex).setCellValue("todo:number_of_views");
        row.createCell(++columnIndex).setCellValue(ad.getAddress());
        row.createCell(++columnIndex).setCellValue(ad.getState());
        row.createCell(++columnIndex).setCellValue(ad.getCity());
        row.createCell(++columnIndex).setCellValue(ad.getPostalCode());
        row.createCell(++columnIndex).setCellValue("todo:age");
        row.createCell(++columnIndex).setCellValue(ad.getDescription());
        row.createCell(++columnIndex).setCellValue(ad.getBedRooms());
        row.createCell(++columnIndex).setCellValue(ad.getBathRooms());
        row.createCell(++columnIndex).setCellValue(ad.getSize());
        row.createCell(++columnIndex).setCellValue(ad.getPrice());
        row.createCell(++columnIndex).setCellValue(ad.getEnergyCertification());
        row.createCell(++columnIndex).setCellValue(ad.getProfessional());
        row.createCell(++columnIndex).setCellValue(ad.getAgent());
        row.createCell(++columnIndex).setCellValue(ad.getAgentPhone());
        row.createCell(++columnIndex).setCellValue("todo:email_listing_agent");
        row.createCell(++columnIndex).setCellValue(ad.getUrl().toString());
        row.createCell(++columnIndex).setCellValue(ad.isHasImages());
        fillInTags(row, ad.getTags(), ++columnIndex);
    }

    private void fillInTags(Row row, List<String> tags, int startIndex)
    {
        for (String tag : tags)
        {
            row.createCell(startIndex).setCellValue(tag);
            startIndex++;
        }
    }

    private void createHeader(Row row)
    {
        int index = 0;
        for (XlsHeader header : XlsHeader.values())
        {
            Cell cell = row.createCell(index);
            cell.setCellValue(header.name());
            index++;
        }
    }
}
