package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyun.oss.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.uitls.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

    @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){

        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");
        String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);

        if (validateCode!=null && validateCodeRedis!=null && validateCode.equals(validateCodeRedis)){
            //验证成功,进行业务操作
            Result result = null;
            try {
                map.put("orderType",Order.ORDERTYPE_WEIXIN);
                result = orderService.order(map);
            }catch (Exception e){
                e.printStackTrace();
                return result;
            }

            if (result.isFlag()){
                try {
                    SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone, (String) map.get("orderDate"));
                } catch (com.aliyuncs.exceptions.ClientException e) {
                    e.printStackTrace();
                }

            }

            return result;

        }else {
            //验证失败
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            Map map = orderService.findById(id);
            return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
        }
    }
}
