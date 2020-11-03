package com.itheima.jobs;

import com.itheima.constant.RedisConstant;
import com.itheima.utils.QiniuUtils;
import org.springframework.beans.factory.annotation.Autowired;
import redis.clients.jedis.JedisPool;

import java.util.Set;

public class ClearImgJob {

    @Autowired
    private JedisPool jedisPool;

    public void clearImg(){
          //根据Redis中的集合 来删除
        Set<String> set = jedisPool.getResource().sdiff(RedisConstant.SETMEAL_PIC_RESOURCES, RedisConstant.SETMEAL_PIC_DB_RESOURCES);
        for (String picName : set) {
            //lai 删除
            QiniuUtils.deleteFileFromQiniu(picName);
            jedisPool.getResource().srem(RedisConstant.SETMEAL_PIC_RESOURCES,picName);
            System.out.println("自定义清理垃圾图片"+picName);
        }

    }
}
