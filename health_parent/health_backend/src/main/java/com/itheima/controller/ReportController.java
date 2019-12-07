package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.service.MemberService;
import com.itheima.service.ReportService;
import com.itheima.service.SetmealService;
import com.itheima.uitls.POIUtils;
import com.sun.org.apache.bcel.internal.generic.NEW;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

@RestController
@RequestMapping("/report")
public class ReportController {

    @Reference
    private MemberService memberService;

    @Reference
    private SetmealService setmealService;

    @Reference
    private ReportService reportService;

    @RequestMapping("/getMemberReport")
    public Result getMemberReport(){

        Map<String,Object> map = new HashMap<>();

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MONTH, -12);

        List<String> months = new ArrayList<>();
        for (int i = 0; i <12 ; i++) {
            calendar.add(Calendar.MONTH, 1);
            Date time = calendar.getTime();
            months.add(new SimpleDateFormat("yyyy.MM").format(time));
        }
        map.put("months",months);

        List<Integer> memberCountList = memberService.findMemberCountByMonth(months);
        map.put("memberCount",memberCountList);

        return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,map);
    }

    @RequestMapping("/getSetmealReport")
    public Result getSetmealReport(){

        Map<String,Object> map = new HashMap<>();

        List<String> setMealNamesList = new ArrayList<>();
        try {
            List<Map<String,Object>> setmealCount = setmealService.findSetmealCount();
            map.put("setmealCount",setmealCount);
            for (Map<String, Object> setMealNameAndCountMap : setmealCount) {
                String name = (String) setMealNameAndCountMap.get("name");  //获取查询到的套餐名称
                setMealNamesList.add(name);
            }
            map.put("setmealNames",setMealNamesList);

            return new Result(true,MessageConstant.GET_SETMEAL_COUNT_REPORT_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_SETMEAL_COUNT_REPORT_FAIL);
        }
    }

    @RequestMapping("/getBusinessReportData")
    public Result getBusinessReportData(){
        try {
            Map<String, Object> data = reportService.getBusinessReportData();
            return new Result(true,MessageConstant.GET_BUSINESS_REPORT_SUCCESS,data);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }

    @RequestMapping("/exportBusinessReport")
    public Result exportBusinessReport(HttpServletRequest request, HttpServletResponse response){
        try {
            Map<String, Object> map = reportService.getBusinessReportData();
            String reportDate = (String) map.get("reportDate");
            Integer todayNewMember = Integer.parseInt(String.valueOf(map.get("todayNewMember")));
            Integer totalMember = Integer.parseInt(String.valueOf(map.get("totalMember")));
            Integer thisWeekNewMember = Integer.parseInt(String.valueOf(map.get("thisWeekNewMember")));
            Integer thisMonthNewMember = Integer.parseInt(String.valueOf(map.get("thisMonthNewMember")));
            Integer todayOrderNumber = Integer.parseInt(String.valueOf(map.get("todayOrderNumber")));
            Integer todayVisitsNumber = Integer.parseInt(String.valueOf(map.get("todayVisitsNumber")));
            Integer thisWeekOrderNumber = Integer.parseInt(String.valueOf(map.get("thisWeekOrderNumber")));
            Integer thisWeekVisitsNumber = Integer.parseInt(String.valueOf(map.get("thisWeekVisitsNumber")));
            Integer thisMonthOrderNumber = Integer.parseInt(String.valueOf(map.get("thisMonthOrderNumber")));
            Integer thisMonthVisitsNumber = Integer.parseInt(String.valueOf(map.get("thisMonthVisitsNumber")));
            List<Map> hotSetmeal = (List<Map>) map.get("hotSetmeal");

            String path = request.getSession().getServletContext().getRealPath("template") + File.separator + "report_template.xlsx";
            XSSFWorkbook workbook = new XSSFWorkbook(new FileInputStream(new File(path)));
            XSSFSheet sheet = workbook.getSheetAt(0);
            sheet.getRow(2).getCell(5).setCellValue(reportDate);
            sheet.getRow(4).getCell(5).setCellValue(todayNewMember);
            sheet.getRow(4).getCell(7).setCellValue(totalMember);
            sheet.getRow(5).getCell(5).setCellValue(thisWeekNewMember);
            sheet.getRow(5).getCell(7).setCellValue(thisMonthNewMember);
            sheet.getRow(7).getCell(5).setCellValue(todayOrderNumber);
            sheet.getRow(7).getCell(7).setCellValue(todayVisitsNumber);
            sheet.getRow(8).getCell(5).setCellValue(thisWeekOrderNumber);
            sheet.getRow(8).getCell(7).setCellValue(thisWeekVisitsNumber);
            sheet.getRow(9).getCell(5).setCellValue(thisMonthOrderNumber);
            sheet.getRow(9).getCell(7).setCellValue(thisMonthVisitsNumber);

            int line = 12;
            XSSFRow row = null;
            for (Map setmeal : hotSetmeal) {
                row = sheet.getRow(line++);
                row.getCell(4).setCellValue(String.valueOf(setmeal.get("name")));
                row.getCell(5).setCellValue(String.valueOf(setmeal.get("setmeal_count")));
                row.getCell(6).setCellValue(String.valueOf(setmeal.get("proportion")));
            }

            response.setContentType("application/octet-stream");
            response.setHeader("content-Disposition","attachment;filename=report.xlsx");

            OutputStream outputStream = response.getOutputStream();
            workbook.write(outputStream);
            outputStream.flush();
            outputStream.close();
            workbook.close();
            return null;
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_BUSINESS_REPORT_FAIL);
        }
    }
}
