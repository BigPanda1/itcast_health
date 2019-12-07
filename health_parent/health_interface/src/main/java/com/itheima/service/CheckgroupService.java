package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;

import java.util.List;

public interface CheckgroupService {

    void add(Integer[] ids, CheckGroup checkGroup);

    PageResult queryPage(QueryPageBean queryPageBean);

    CheckGroup findById(Integer id);

    List<Integer> findCheckitemIdsByCheckgroupId(Integer id);

    void edit(Integer[] ids, CheckGroup checkGroup);

    void delete(Integer id);

    List<CheckGroup> findAll();
}
