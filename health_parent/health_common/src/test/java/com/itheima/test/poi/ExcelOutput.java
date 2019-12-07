package com.itheima.test.poi;

import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class ExcelOutput {

    @Test
    public void test1() throws IOException {

        XSSFWorkbook workbook = new XSSFWorkbook();
        XSSFSheet sheet = workbook.createSheet("黑马程序员");

        XSSFRow row = sheet.createRow(0);
        row.createCell(0).setCellValue("姓名");
        row.createCell(1).setCellValue("地址");
        row.createCell(2).setCellValue("年龄");

        XSSFRow row2 = sheet.createRow(1);
        row2.createCell(0).setCellValue("张三");
        row2.createCell(1).setCellValue("北京");
        row2.createCell(2).setCellValue("22");

        XSSFRow row3 = sheet.createRow(2);
        row3.createCell(0).setCellValue("李四");
        row3.createCell(1).setCellValue("广州");
        row3.createCell(2).setCellValue("33");

        FileOutputStream fis = new FileOutputStream(new File("E:\\test\\poiOut.xlsx"));
        workbook.write(fis);
        fis.flush();
        fis.close();
    }
}
