package com.itheima.service.impl;


import com.alibaba.dubbo.config.annotation.Service;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.itheima.dao.CheckGroupDao;
import com.itheima.entity.PageResult;
import com.itheima.entity.QueryPageBean;
import com.itheima.pojo.CheckGroup;
import com.itheima.service.CheckGroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = CheckGroupService.class)
@Transactional
public class CheckGroupServiceImpl implements CheckGroupService {

    @Autowired
    private CheckGroupDao checkGroupDao;

    @Override
    public void add(CheckGroup checkGroup,Integer[] checkitemIds){
         checkGroupDao.add(checkGroup);
         Integer checkgroupId=checkGroup.getId();
         this.setCheckGroupAndCheckItem(checkitemIds, checkgroupId);
    }

    @Override
    public PageResult findPage(QueryPageBean queryPageBean) {
        Integer currentPage=queryPageBean.getCurrentPage();
        Integer pageSize = queryPageBean.getPageSize();
        String queryString =queryPageBean.getQueryString();
        PageHelper.startPage(currentPage,pageSize);
        Page<CheckGroup> page=checkGroupDao.findByCondition(queryString);
        return new PageResult(page.getTotal(),page.getResult());
    }

    @Override
    public CheckGroup findById(Integer id) {

        return checkGroupDao.findById(id);
    }

    @Override
    public List<Integer> findCheckItemIdsByCheckGroupId(Integer id) {
        return checkGroupDao.findCheckItemIdsByCheckGroupId(id);
    }

    @Override
    public void edit(CheckGroup group, Integer[] checkitemIds) {
       checkGroupDao.edit(group);
       Integer id=group.getId();
       checkGroupDao.deleteAssocication(id);
       this.setCheckGroupAndCheckItem(checkitemIds,id);

    }

    @Override
    public List<CheckGroup> findAll() {
        return checkGroupDao.findAll();
    }

    public void setCheckGroupAndCheckItem(Integer[] checkitemIds, Integer checkgroupId) {
        if(checkitemIds!=null && checkitemIds.length>0){
            for (Integer checkitemId : checkitemIds) {
                Map<String,Integer> map=new HashMap<>();
                map.put("checkgroupId",checkgroupId);
                map.put("checkitemId",checkitemId);
                checkGroupDao.setCheckGroupAndCheckItemId(map);
            }
        }
    }
}
