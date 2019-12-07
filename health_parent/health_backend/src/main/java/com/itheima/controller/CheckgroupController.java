package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckgroupService;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/checkgroup")
public class CheckgroupController {

    @Reference
    private CheckgroupService checkgroupService;

    @RequestMapping("/add")
    public Result add(Integer[] ids,@RequestBody CheckGroup checkGroup){

        try {
            checkgroupService.add(ids,checkGroup);
        }catch (Exception e){
            return new Result(false,MessageConstant.ADD_CHECKGROUP_FAIL);
        }

        return new Result(true,MessageConstant.ADD_CHECKGROUP_SUCCESS);

    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){
            return checkgroupService.queryPage(queryPageBean);
    }

    @RequestMapping("/findById")
    public Result findById(Integer id){
        try {
            CheckGroup checkGroup = checkgroupService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,checkGroup);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/findCheckitemIdsByCheckgroupId")
    public Result findCheckitemIdsByCheckgroupId(Integer id){
        try {
            List<Integer> cheitemIdList = checkgroupService.findCheckitemIdsByCheckgroupId(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,cheitemIdList);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }
    }

    @RequestMapping("/edit")
    public Result edit(Integer[] ids,@RequestBody CheckGroup checkGroup){
        try {
            checkgroupService.edit(ids,checkGroup);
            return new Result(true,MessageConstant.EDIT_CHECKGROUP_SUCCESS);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKGROUP_FAIL);
        }
    }

    @RequestMapping("/delete")
    public Result delete(Integer id){
        try {
            checkgroupService.delete(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKGROUP_FAIL);
        }
        return new Result(true,MessageConstant.DELETE_CHECKGROUP_SUCCESS);
    }

    @RequestMapping("/findAll")
    public Result findAll(){
        try {
            List<CheckGroup> list = checkgroupService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKGROUP_SUCCESS,list);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.QUERY_CHECKGROUP_FAIL);
        }

    }
}
