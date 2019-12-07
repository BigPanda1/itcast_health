package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.itheima.constant.MessageConstant;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.entity.Result;
import com.itheima.pojo.CheckItem;
import com.itheima.service.CheckitemService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.awt.*;
import java.util.List;

@RestController
@RequestMapping("/checkitem")
public class CheckitemController {

    @Reference
    private CheckitemService checkitemService;

    @RequestMapping("/add")
    public Result add(@RequestBody CheckItem checkItem){

        try {
            checkitemService.add(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.ADD_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.ADD_CHECKITEM_SUCCESS);

    }

    @RequestMapping("/findPage")
    public PageResult findPage(@RequestBody QueryPageBean queryPageBean){

        return checkitemService.findPage(queryPageBean);

    }

    @RequestMapping("/delete")
    @PreAuthorize("hasAuthority('CHECKITEM_DELETE')")
    public Result delete(Integer id){

        try {
            checkitemService.deleteByCheckitemId(id);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.DELETE_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.DELETE_CHECKITEM_SUCCESS);

    }

    @RequestMapping("/edit")
    public Result edit(@RequestBody CheckItem checkItem){

        try {
            checkitemService.edit(checkItem);
        }catch (Exception e){
            e.printStackTrace();
            return new Result(false,MessageConstant.EDIT_CHECKITEM_FAIL);
        }

        return new Result(true,MessageConstant.EDIT_CHECKITEM_SUCCESS);

    }

    @RequestMapping("/findById")
    public Result findById(Integer id){

        try {
            CheckItem checkItem = checkitemService.findById(id);
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,checkItem);
        }catch (Exception e){

            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }

    }

    @RequestMapping("/findAll")
    public Result findAll(){

        try {
            List<CheckItem> list = checkitemService.findAll();
            return new Result(true,MessageConstant.QUERY_CHECKITEM_SUCCESS,list);
        }catch (Exception e){

            return new Result(false,MessageConstant.QUERY_CHECKITEM_FAIL);
        }


    }
}
