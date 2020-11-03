package com.itheima.dao;

import com.github.pagehelper.Page;
import com.itheima.pojo.CheckGroup;

import java.util.List;
import java.util.Map;

public interface CheckGroupDao {

     void add(CheckGroup checkGroup);
     void setCheckGroupAndCheckItemId(Map map);
     Page<CheckGroup> findByCondition(String queryString);

     CheckGroup findById(Integer id);

     List<Integer> findCheckItemIdsByCheckGroupId(Integer id);

     void edit(CheckGroup group);

     void deleteAssocication(Integer id);

    List<CheckGroup> findAll();
}
