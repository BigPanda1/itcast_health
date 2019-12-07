package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface CheckgroupDao {

    void add(CheckGroup checkGroup);

    void addCheckgroupAndCheckitem(Map<String,Integer> map);

    Page<CheckGroup> findCondition(String queryString);

    CheckGroup findById(Integer id);

    List<Integer> findCheckitemIdsByCheckgroupId(Integer id);

    void updateCheckgroup(CheckGroup checkGroup);

    void clearCheckgroupAndCheckitem(Integer id);

    void deleteCheckgroup(Integer id);

    List<CheckGroup> findAll();


}
