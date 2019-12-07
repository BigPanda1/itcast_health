package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.alibaba.fastjson.JSON;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.Map;

@RestController
@RequestMapping("/member")
public class MemberController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private MemberService memberService;

    @RequestMapping("/login")
    public Result login(@RequestBody Map map, HttpServletResponse response){

        String telephone = (String) map.get("telephone");
        String validateCode = (String) map.get("validateCode");  //获取用户输入的验证码
        String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_LOGIN);

        if (validateCode != null && validateCodeRedis != null && validateCode.equals(validateCodeRedis)){
            //验证码正确
            Member member = memberService.findByTelephone(telephone);
            if (member == null){
                //用户不是会员,自动注册
                member = new Member();
                member.setRegTime(new Date());
                member.setPhoneNumber(telephone);
                memberService.add(member);
            }

            Cookie cookie = new Cookie("login_member_telephone",telephone); //往客户端浏览器写入cookie跟踪用户
            cookie.setPath("/");
            cookie.setMaxAge(60*60*24*30);
            response.addCookie(cookie);

            //将用户信息保存到redis
            String json_member = JSON.toJSON(member).toString();
            jedisPool.getResource().setex(telephone,60*30,json_member);

            return new Result(true,MessageConstant.LOGIN_SUCCESS);
        }else {
            //验证码错误
            return new Result(false,MessageConstant.VALIDATECODE_ERROR);
        }
    }
}
