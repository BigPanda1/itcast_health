package com.itheima.test.poi;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExcelInput {

    @Test
    public void test1() throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:\\test\\测试表格.xlsx")));

        XSSFSheet sheet = workbook.getSheetAt(0);

        for (Row row : sheet) {
            for (Cell cell : row) {
                System.out.print(cell+"\t");
            }
            System.out.println();
        }

        workbook.close();
    }

    @Test
    public void test2() throws IOException {
        XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File("E:\\test\\测试表格.xlsx")));

        XSSFSheet sheet = workbook.getSheetAt(0);

        int rowNum = sheet.getLastRowNum();
        for (int i = 0; i <= rowNum; i++) {

            XSSFRow row = sheet.getRow(i);
            short cellNum = row.getLastCellNum();
            for (int j = 0; j <cellNum ; j++) {
                XSSFCell cell = row.getCell(j);
                System.out.print(cell);
            }
            System.out.println();

        }

        workbook.close();
    }
}
