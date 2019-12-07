package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.service.OrderSettingService;
import com.itheima.uitls.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderSettingDao settingDao;

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Result order(Map map) throws Exception {

        String orderDate = (String) map.get("orderDate");
        //判断当天能不能预约
        OrderSetting orderSetting = settingDao.findOrdersetByOrderDate(DateUtils.parseString2Date(orderDate));
        if (orderSetting == null){
            return new Result(false,MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }

        //判断当天是否约满
        int number = orderSetting.getNumber();  //可预约人数
        int reservations = orderSetting.getReservations();  //已预约人数
        if (reservations >= number || number ==0){
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //判断用户是否重复预约同一时期同一套餐
        String telephone = (String) map.get("telephone");
        Member member = memberDao.findByTelephone(telephone);

        if (member != null){
            Order order = new Order(member.getId(),DateUtils.parseString2Date(orderDate), Integer.parseInt((String) map.get("setmealId")));
            List<Order> orderList = orderDao.findByCondition(order);
            if (orderList != null && orderList.size()>0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }

        }else {
            //如果用户没注册,就自动注册
            member = new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setRegTime(new Date());
            member.setIdCard((String) map.get("idCard"));
            member.setSex((String) map.get("sex"));
            memberDao.add(member);
        }

        Order order = new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations() +1);
        settingDao.editReservationsByOrderDate(orderSetting);

        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) throws Exception {

        Map map = orderDao.findById4Detail(id);
        if (map != null) {
            Date orderDate = (Date) map.get("orderDate");
            String orderDate_Str = DateUtils.parseDate2String(orderDate);

            map.put("orderDate",orderDate_Str);
        }
        return map;
    }
}
