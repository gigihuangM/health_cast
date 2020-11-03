package com.itheima.service;

import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealService {
    void add(Setmeal setmeal, Integer[] checkgroupIds);


    PageResult findPage(QueryPageBean queryPageBean);


    List<Setmeal> getAll();

    Setmeal findById(int id);

    List<Map<String, Object>> findSetmealCount();
}
