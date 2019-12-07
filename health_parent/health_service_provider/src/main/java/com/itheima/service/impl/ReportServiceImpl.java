package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.service.ReportService;
import com.itheima.uitls.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = ReportService.class)
@Transactional
public class ReportServiceImpl implements ReportService {

    @Autowired
    private MemberDao memberDao;

    @Autowired
    private OrderDao orderDao;

    @Override
    public Map<String, Object> getBusinessReportData() throws Exception {
        Map<String,Object> map = new HashMap<>();
        String today = DateUtils.parseDate2String(DateUtils.getToday());//今天的日期
        map.put("reportDate", today);

        Integer todayNewMemberCount = memberDao.findMemberCountByDate(today);  //今日注册的会员数量

        Integer totaoMemberCount = memberDao.findMemberTotalCount();  //总会员数量

        String thisWeekMonday = DateUtils.parseDate2String(DateUtils.getThisWeekMonday());  //获得本周一
        Integer memberCountWeek = memberDao.findMemberCountAfterDate(thisWeekMonday);  //本周新会员数量

        String thisMonthMonday = DateUtils.parseDate2String(DateUtils.getFirstDay4ThisMonth());  //获得本月第一天
        Integer memberCountMonth = memberDao.findMemberCountAfterDate(thisMonthMonday);   //获得本月新会员数量

        Integer todayOrderCount = orderDao.findOrderCountByDate(today);   //今日预约数量

        Integer todayVisitsCount = orderDao.findVisitsCountByDate(today);  //今日到诊数量

        Integer orderCountWeek = orderDao.findOrderCountAfterDate(thisWeekMonday);   //本周预约数量

        Integer visitsCountWeek = orderDao.findVisitsCountAfterDate(thisWeekMonday);  //本周到诊数量

        Integer orderCountMonth = orderDao.findOrderCountAfterDate(thisMonthMonday);   //本月预约数量

        Integer visitsCountMonth = orderDao.findVisitsCountAfterDate(thisMonthMonday);  //本月到诊数量

        List<Map> hotSetmeal = orderDao.findHotSetmeal();   //查询热门套餐数据

        map.put("todayNewMember",todayNewMemberCount);
        map.put("totalMember",totaoMemberCount);
        map.put("thisWeekNewMember",memberCountWeek);
        map.put("thisMonthNewMember",memberCountMonth);
        map.put("todayOrderNumber",todayOrderCount);
        map.put("todayVisitsNumber",todayVisitsCount);
        map.put("thisWeekOrderNumber",orderCountWeek);
        map.put("thisWeekVisitsNumber",visitsCountWeek);
        map.put("thisMonthOrderNumber",orderCountMonth);
        map.put("thisMonthVisitsNumber",visitsCountMonth);
        map.put("hotSetmeal",hotSetmeal);


        return map;
    }
}
