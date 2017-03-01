package com.idealista.scraper.xls;

import com.idealista.scraper.model.Advertisment;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.Set;

public class XlsExporter
{
    private static final String IDEALISTA_DATA = "Idealista_data";
    private static final Logger LOGGER = LogManager.getLogger(XlsExporter.class);
    private Workbook wb;
    private String fileName;

    public XlsExporter(String fileName)
    {
        this.fileName = fileName;
        createWorkBook();
    }

    private void createWorkBook()
    {
        try (FileOutputStream fileOut = new FileOutputStream(fileName))
        {
            wb = new XSSFWorkbook();
            Sheet sheet = wb.createSheet(IDEALISTA_DATA);
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

    public void appendResults(Set<Advertisment> advertisments)
    {
        LOGGER.info("Writing new <{}> advertisments to XLS...", advertisments.size());
        try (FileOutputStream fileOut = new FileOutputStream(fileName))
        {
            Sheet sheet = wb.getSheet(IDEALISTA_DATA);
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
        row.createCell(0).setCellValue(ad.getTitle());
        row.createCell(1).setCellValue(ad.getType());
        row.createCell(2).setCellValue(ad.getSubType().name());
        row.createCell(3).setCellValue(ad.getDateOfListing());
        row.createCell(4).setCellValue("todo:number_of_views");
        row.createCell(5).setCellValue(ad.getAddress());
        row.createCell(6).setCellValue(ad.getState());
        row.createCell(7).setCellValue(ad.getCity());
        row.createCell(8).setCellValue(ad.getPostalCode());
        row.createCell(9).setCellValue("todo:age");
        row.createCell(10).setCellValue(ad.getDescription());
        row.createCell(11).setCellValue(ad.getBedRooms());
        row.createCell(12).setCellValue(ad.getBathRooms());
        row.createCell(13).setCellValue(ad.getSize());
        row.createCell(14).setCellValue(ad.getPrice());
        row.createCell(15).setCellValue(ad.getEnergyCertification());
        row.createCell(16).setCellValue(ad.getProfessional());
        row.createCell(17).setCellValue(ad.getAgent());
        row.createCell(18).setCellValue(ad.getAgentPhone());
        row.createCell(19).setCellValue("todo:email_listing_agent");
        row.createCell(20).setCellValue(ad.getUrl().toString());
        row.createCell(21).setCellValue(ad.isHasImages());
        fillInTags(row, ad.getTags());
    }

    private void fillInTags(Row row, List<String> tags)
    {
        int index = 22;
        for (String tag : tags)
        {
            row.createCell(index).setCellValue(tag);
            index++;
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
