package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckitemDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service(interfaceClass = CheckitemService.class)
@Transactional
public class CheckitemServiceImpl implements CheckitemService {

    @Autowired
    private CheckitemDao checkitemDao;

    @Override
    public void add(CheckItem checkItem) {
        checkitemDao.add(checkItem);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage,pageSize);

        Page<CheckItem> page = checkitemDao.selectByConditon(queryString);
        long total = page.getTotal(); //总记录数

        List<CheckItem> rows = page.getResult(); //当前页面的数据

        return new PageResult(total,rows);
    }

    @Override
    public void deleteByCheckitemId(Integer id) {

        long count = checkitemDao.findCountByCheckitemId(id);
        if (count >0){

            new RuntimeException();
        }

        checkitemDao.deleteCheckitem(id);
    }

    @Override
    public void edit(CheckItem checkItem) {

        checkitemDao.updateCheckitem(checkItem);
    }

    @Override
    public CheckItem findById(Integer id) {
        return checkitemDao.findById(id);
    }

    @Override
    public List<CheckItem> findAll() {
        return checkitemDao.findAll();
    }


}
