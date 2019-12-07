package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.OrderSettingDao;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderSettingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderSettingService.class)
@Transactional
public class OrderSettingServiceImpl implements OrderSettingService {

    @Autowired
    private OrderSettingDao settingDao;

    @Override
    public void add(List<OrderSetting> list) {

        if (list!=null && list.size()>0){
            for (OrderSetting orderSetting : list) {

                long count = settingDao.findByOrderDate(orderSetting.getOrderDate());
                if (count>0){
                    //代表当前日期已经添加过预约信息,要执行修改预约人数操作
                    settingDao.editNumberByOrderDate(orderSetting);
                }else {
                    settingDao.add(orderSetting);
                }
            }
        }

    }

    @Override
    public List<Map> findOrderSettingByMonth(String date) {  //2019-1
        String newDate = date.split("-")[1];
        if (Integer.parseInt(newDate)>=1 && Integer.parseInt(newDate)<=9){
            newDate ="0"+newDate; //01
            date = date.split("-")[0]+"-"+newDate;
        }
        List<OrderSetting> settingList = settingDao.findByMonth(date);
        List<Map> list = new ArrayList<>();

        if (settingList!=null && settingList.size()>0){
            for (OrderSetting orderSetting : settingList) {
                Map<String,Object> map = new HashMap<>();
                map.put("date",orderSetting.getOrderDate().getDate());
                map.put("number",orderSetting.getNumber());
                map.put("reservations",orderSetting.getReservations());

                list.add(map);
            }
        }
        return list;
    }

    @Override
    public void editNumber(OrderSetting orderSetting) {

        long count = settingDao.findByOrderDate(orderSetting.getOrderDate());
        if (count>0){
            settingDao.editNumberByOrderDate(orderSetting);
        }else {
            settingDao.add(orderSetting);
        }

    }
}
