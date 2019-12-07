package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.constant.RedisConstant;
import com.itheima.dao.SetmealDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;
import com.itheima.service.SetmealService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import redis.clients.jedis.JedisPool;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = SetmealService.class)
@Transactional
public class SetmealServiceImpl implements SetmealService {

    @Autowired
    private  SetmealDao setmealDao;

    @Autowired
    private JedisPool jedisPool;

    @Override
    public void add(Integer[] ids, Setmeal setmeal) {

        setmealDao.add(setmeal);

        jedisPool.getResource().sadd(RedisConstant.SETMEAL_PIC_DB_RESOURCES,setmeal.getImg());
        Integer setmealId = setmeal.getId();

        this.addSetmealAndCheckgroup(setmealId,ids);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage,pageSize);

        Page<Setmeal> page = setmealDao.findPage(queryString);
        List<Setmeal> rows = page.getResult();
        long total = page.getTotal();
        return new PageResult(total,rows);
    }

    @Override
    public List<Setmeal> findAll() {
        return setmealDao.findAll();
    }

    @Override
    public Setmeal findById(Integer id) {
        return setmealDao.findById(id);
    }

    @Override
    public List<Map<String, Object>> findSetmealCount() {
        return setmealDao.findSetMealNameAndCount();
    }

    //设置检查套餐和检查组关联关系
    private  void addSetmealAndCheckgroup(Integer setMealId,Integer[] ids){

        if (ids!=null && ids.length>0){
            for (Integer id : ids) {
                Map<String,Integer> map = new HashMap<>();
                map.put("setMealId",setMealId);
                map.put("checkGroupId",id);

                setmealDao.addSetmealAndCheckgroup(map);
            }
        }
    }
}
