package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;

import java.awt.*;
import java.util.List;

public interface CheckitemService {

    void add(CheckItem checkItem);

    PageResult findPage(QueryPageBean queryPageBean);

    void deleteByCheckitemId(Integer id);

    void edit(CheckItem checkItem);

    CheckItem findById(Integer id);


    List<CheckItem> findAll();
}
