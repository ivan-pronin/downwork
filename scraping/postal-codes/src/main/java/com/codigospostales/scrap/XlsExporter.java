package com.codigospostales.scrap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Component;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Set;

@Component
public class XlsExporter
{
    private Workbook wb;
    private Sheet sheet;

    public void writeResultsToXls(Set<PostalCode> data)
    {
        try (FileOutputStream fileOut = new FileOutputStream("postalCodesAll.xlsx"))
        {
            wb = new XSSFWorkbook();
            sheet = wb.createSheet("PostalCodes");
            Row row = sheet.createRow((short) 0);
            createHeader(row);
            for (PostalCode postalCode : data)
            {
                writePostalCodeToRow(postalCode, sheet.createRow(sheet.getLastRowNum()));
            }
            wb.write(fileOut);
        }
        catch (IOException e)
        {
            System.err.println("Error while writing new results to XLS: {}" + e);
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

    private void writePostalCodeToRow(PostalCode item, Row row)
    {
        if (item == null)
        {
            System.err.println("Could not write PostalCode to XLS - it's NULL");
            return;
        }
        if (row == null)
        {
            System.err.println("Could not write PostalCode to XLS - row is NULL");
            return;
        }
        
        for (String street : item.getStreets())
        {
            String zipCode = item.getNumber();
            row.createCell(0).setCellValue(street);
            row.createCell(1).setCellValue(zipCode);
            row.createCell(2).setCellValue(item.getProvince());
            row = sheet.createRow(row.getRowNum() + 1);
        }
    }
}
