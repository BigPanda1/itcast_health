package com.itheima.dao;

import com.itheima.pojo.OrderSetting;

import java.util.Date;
import java.util.List;

public interface OrderSettingDao {

    void add(OrderSetting orderSetting);
    void editNumberByOrderDate(OrderSetting orderSetting);
    long findByOrderDate(Date date);

    List<OrderSetting> findByMonth(String date);

    OrderSetting findOrdersetByOrderDate(Date orderDate);

    //更新已预约人数
    public void editReservationsByOrderDate(OrderSetting orderSetting);
}
