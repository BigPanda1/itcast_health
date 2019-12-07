package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;
import org.springframework.stereotype.Repository;

import java.util.List;


public interface CheckitemDao {

    void add(CheckItem checkItem);

    Page<CheckItem> selectByConditon(String queryString);

    long findCountByCheckitemId(Integer id);

    void deleteCheckitem(Integer id);

    void updateCheckitem(CheckItem checkItem);

    CheckItem findById(Integer id);


    List<CheckItem> findAll();
}
