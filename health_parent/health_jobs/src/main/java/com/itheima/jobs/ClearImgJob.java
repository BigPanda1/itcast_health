package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.uitls.AliyunUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){

        Jedis jedis = jedisPool.getResource();
        Set<String> set = jedis.sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);

        if (set!=null){
            for (String imgName : set) {
                AliyunUtils.deleteFileFromAliyun(imgName);
                jedis.srem(RedisConstant.SETMEAL_PIC_RESOURCES,imgName);
                System.out.println("清理垃圾图片"+imgName);
            }
        }
//        jedis.close();
    }
}
