package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import com.itheima.uitls.POIUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ordersetting")
public class OrdersettingController {

    @Reference
    private OrderSettingService settingService;

    @RequestMapping("/upload")
    public Result upload(@RequestParam("excelFile") MultipartFile multipartFile){

        try {
            List<String[]> excel = POIUtils.readExcel(multipartFile);
            List<OrderSetting> orderSettingList = new ArrayList<>();

            for (String[] strings : excel) {
                String date = strings[0];
                String number = strings[1];
                OrderSetting orderSetting = new OrderSetting(new Date(date),Integer.parseInt(number));

                orderSettingList.add(orderSetting);
            }

            settingService.add(orderSettingList);
            return new Result(true,MessageConstant.IMPORT_ORDERSETTING_SUCCESS);
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false,MessageConstant.IMPORT_ORDERSETTING_FAIL);
        }
    }

    @RequestMapping("/findOrderSettingByMonth")
    public Result findOrderSettingByMonth(String date){
        try {
            List<Map> list = settingService.findOrderSettingByMonth(date);
            return new Result(true,MessageConstant.GET_ORDERSETTING_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.GET_ORDERSETTING_FAIL);
        }

    }

    @RequestMapping("/editNumber")
    public Result editNumber(@RequestBody OrderSetting orderSetting){
        try {
            settingService.editNumber(orderSetting);
            return new Result(true,MessageConstant.ORDERSETTING_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ORDERSETTING_FAIL);
        }
    }

}
