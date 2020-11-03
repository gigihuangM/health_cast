package com.itheima.controller;

import com.aliyuncs.exceptions.ClientException;
import com.itheima.constant.MessageConstant;
import com.itheima.constant.RedisConstant;
import com.itheima.constant.RedisMessageConstant;
import com.itheima.entity.Result;
import com.itheima.utils.SMSUtils;
import com.itheima.utils.ValidateCodeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import redis.clients.jedis.JedisPool;

import java.util.*;

@RestController
@RequestMapping("/validateCode")
public class validateCodeController {

    @Autowired
    private JedisPool jedisPool;
    //发送验证码
    @RequestMapping("/send4Order")
    public Result send4Order(String telephone){
        Integer validateCode = ValidateCodeUtils.generateValidateCode(4);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_ORDER,300,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    @RequestMapping("/send4Login")
    public Result send4Login(String telephone){
        Integer validateCode = ValidateCodeUtils.generateValidateCode(6);
        try {
            SMSUtils.sendShortMessage(SMSUtils.VALIDATE_CODE,telephone,validateCode.toString());
        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false, MessageConstant.SEND_VALIDATECODE_FAIL);
        }
        jedisPool.getResource().setex(telephone+ RedisMessageConstant.SENDTYPE_LOGIN,350,validateCode.toString());
        return new Result(true,MessageConstant.SEND_VALIDATECODE_SUCCESS);
    }

    public static void main(String[] args) {
        int a[]={1,5,8,11,16,17,29,34,39};
        int time_line[]={3,7,12,17,19,31,40};
        int index=0;
        int j=0;
        for (int t = 0; t < a.length-1; t++) {
        for (int i = 0; i < time_line.length; i++) {
            int rs=Math.abs(a[t]-time_line[i]);
            while(index==0){
                index=rs;
                j=i;
            }
            if(index>rs){
                index=rs;
                j=i;
            }
            if(rs==index){
               System.out.println("[{line:"+time_line[i]+"}]"+",[{time"+a[t]+"}]");

            }
            if(rs==0){
                System.out.println("[{line:"+time_line[i]+"}]"+",[{time"+a[t+1]+"}]");
            }
        }
            System.out.println("[{line:"+time_line[j]+"}]"+",[{time"+a[t]+"}]");
        }




    }
}



