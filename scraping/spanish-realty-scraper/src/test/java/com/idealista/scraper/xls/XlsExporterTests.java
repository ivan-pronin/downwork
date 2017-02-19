package com.idealista.scraper.xls;

import com.idealista.scraper.model.Advertisment;
import com.idealista.scraper.model.RealtyType;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;

public class XlsExporterTests
{
    // @Test
    public void testExportToXls() throws Exception
    {
        FileOutputStream fileOut = new FileOutputStream("workbook.xlsx");
        Workbook wb = new XSSFWorkbook();
        Sheet sheet = wb.createSheet("new sheet");

        // Create a row and put some cells in it. Rows are 0 based.
        Row row = sheet.createRow((short) 0);
        // Create a cell and put a value in it.
        createHeader(row);
        wb.write(fileOut);
        fileOut.close();
    }

    //@Test
    public void testAppend() throws Exception
    {
        XlsExporter xls = new XlsExporter("test1.xls");
        xls.appendResults(createAds(2));
        xls.appendResults(createAds(3));
        xls.appendResults(createAds(5));
    }

    private Set<Advertisment> createAds(int numberOfAds) throws MalformedURLException
    {
        Set<Advertisment> results = new HashSet<>();
        for (int i = 0; i < numberOfAds; i++)
        {
            results.add(new Advertisment(new URL("http://www.url-" + i + ".com"), "title-" + i, RealtyType.BUILDING));
        }
        return results;
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
