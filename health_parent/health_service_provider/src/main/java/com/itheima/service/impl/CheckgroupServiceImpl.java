package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckgroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckgroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckgroupService.class)
@Transactional
public class CheckgroupServiceImpl implements CheckgroupService {

    @Autowired
    private CheckgroupDao checkgroupDao;

    @Override
    public void add(Integer[] ids, CheckGroup checkGroup) {

        checkgroupDao.add(checkGroup);

        Integer checkGroupId = checkGroup.getId();

        this.addCheckgroupAndCheckitem(ids,checkGroupId);
    }

    @Override
    public PageResult queryPage(QueryPageBean queryPageBean) {
        Integer currentPage = queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString = queryPageBean.getQueryString();

        PageHelper.startPage(currentPage,pageSize);

        Page<CheckGroup> page = checkgroupDao.findCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {
        return checkgroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckitemIdsByCheckgroupId(Integer id) {
        return checkgroupDao.findCheckitemIdsByCheckgroupId(id);
    }

    @Override
    public void edit(Integer[] ids, CheckGroup checkGroup) {

        //修改检查组基本信息
        checkgroupDao.updateCheckgroup(checkGroup);

        //清楚检查组和检查项的关联关系
        checkgroupDao.clearCheckgroupAndCheckitem(checkGroup.getId());

        //重新建立关联关系
        this.addCheckgroupAndCheckitem(ids,checkGroup.getId());
    }

    @Override
    public void delete(Integer id) {
        //先去关系表删除关联关系
        checkgroupDao.clearCheckgroupAndCheckitem(id);

        checkgroupDao.deleteCheckgroup(id);
    }

    @Override
    public List<CheckGroup> findAll() {

        return checkgroupDao.findAll();
    }

    //抽取建立检查项和检查组的关系方法
    private void addCheckgroupAndCheckitem(Integer[] ids,Integer checkGroupId){
        if (ids!=null && ids.length>0) {
            for (Integer id : ids) {
                Map<String, Integer> map = new HashMap<>();
                map.put("checkgroup_id", checkGroupId);
                map.put("checkitem_id", id);
                checkgroupDao.addCheckgroupAndCheckitem(map);
            }
        }
    }
}
