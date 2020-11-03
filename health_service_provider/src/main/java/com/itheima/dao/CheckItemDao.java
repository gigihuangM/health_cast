package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckItem;

import java.util.List;

public interface CheckItemDao {
     void add(CheckItem checkItem);
     Page<CheckItem> selectByCondition(String queryString);
     long findCountByCheckItemId(Integer id);
     void deleteById(Integer id);
     void edit(CheckItem checkItem);
     CheckItem finById(Integer id);
     List<CheckItem> findAll();
}
