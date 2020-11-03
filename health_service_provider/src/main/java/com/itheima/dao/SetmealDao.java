package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.Setmeal;

import java.util.List;
import java.util.Map;

public interface SetmealDao {

    void add(Setmeal setmeal);
    void setSetmealAndCheckGroup(Map map);

    Page<Setmeal> findPage(String queryString);



    List<Setmeal> getAll();

    Setmeal findById(int id);

    List<Map<String, Object>> findSetmealCount();
}
