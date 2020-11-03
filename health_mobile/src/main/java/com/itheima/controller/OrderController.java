package com.itheima.controller;

import com.alibaba.dubbo.config.annotation.Reference;
import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.pojo.Order;
import com.itheima.service.OrderService;
import com.itheima.utils.SMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private JedisPool jedisPool;

    @Reference
    private OrderService orderService;

   @RequestMapping("/submit")
    public Result submit(@RequestBody Map map){
       String telephone = (String) map.get("telephone");
       String validateCodeRedis = jedisPool.getResource().get(telephone + RedisMessageConstant.SENDTYPE_ORDER);
       String validateCode= (String) map.get("validateCode");
       if(validateCodeRedis!=null&&validateCode!=null&&validateCode.equals(validateCodeRedis)){
           map.put("orderType", Order.ORDERTYPE_WEIXIN);
           Result result=null;
           try {
               result = orderService.order(map);
           }catch (Exception e){
               e.printStackTrace();
               return result;
           }
           if(result.isFlag()){
               //预约成功
               try {
                   SMSUtils.sendShortMessage(SMSUtils.ORDER_NOTICE,telephone,(String)map.get("orderDate"));
               } catch (ClientException e) {
                   e.printStackTrace();
               }
           }

           return result;
       }else{
           return new Result(false, MessageConstant.VALIDATECODE_ERROR);
       }
    }
    @RequestMapping("/findById")
    public Result findById(Integer id){
      try{
          Map map= orderService.findById(id);
          return new Result(true,MessageConstant.QUERY_ORDER_SUCCESS,map);
      }catch (Exception e){
          e.printStackTrace();
          return new Result(false,MessageConstant.QUERY_ORDER_FAIL);
      }

    }

}
