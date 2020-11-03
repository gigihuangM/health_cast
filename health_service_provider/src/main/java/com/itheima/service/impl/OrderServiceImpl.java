package com.itheima.service.impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.itheima.constant.MessageConstant;
import com.itheima.dao.MemberDao;
import com.itheima.dao.OrderDao;
import com.itheima.dao.OrderSettingDao;
import com.itheima.entity.Result;
import com.itheima.pojo.Member;
import com.itheima.pojo.Order;
import com.itheima.pojo.OrderSetting;
import com.itheima.service.OrderService;
import com.itheima.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Service(interfaceClass = OrderService.class)
@Transactional
public class OrderServiceImpl implements OrderService  {
    @Autowired
    private OrderSettingDao orderSettingDao;
    @Autowired
    private MemberDao memberDao;
    @Autowired
    private OrderDao orderDao;
    @Override
    public Result order(Map map) throws  Exception{
        String orderDate = (String) map.get("orderDate");
        OrderSetting orderSetting= orderSettingDao.findByOrderDate(orderDate);
        if(orderSetting==null){
            //无法完成预约
            return new Result(false, MessageConstant.SELECTED_DATE_CANNOT_ORDER);
        }
        int number = orderSetting.getNumber(); //可预约人数
        int reservations = orderSetting.getReservations();
        if(reservations>number){
            //已经预约满了
            return new Result(false,MessageConstant.ORDER_FULL);
        }
        //用户是否在重复预约
        String telephone = (String) map.get("telephone");
        Member member=memberDao.findByTelephone(telephone);
        if(member!=null){
            Integer id = member.getId();
            Date order_Date=DateUtils.parseString2Date(orderDate);
            String setmealId = (String) map.get("setmealId");
            Order order = new Order(id,order_Date,Integer.parseInt(setmealId));
            List<Order> list = orderDao.findByCondition(order);
            if(list!=null&&list.size()>0){
                return new Result(false,MessageConstant.HAS_ORDERED);
            }
        }
        //操作的是用户的表
        else
        {
            member=new Member();
            member.setName((String) map.get("name"));
            member.setPhoneNumber(telephone);
            member.setIdCard((String) map.get("idCard"));
            member.setRegTime(new Date());
            memberDao.add(member);
        }
         Order order=new Order();
        order.setMemberId(member.getId());
        order.setOrderDate(DateUtils.parseString2Date(orderDate));
        order.setOrderType((String) map.get("orderType"));
        order.setOrderStatus(Order.ORDERSTATUS_NO);
        order.setSetmealId(Integer.parseInt((String) map.get("setmealId")));
        orderDao.add(order);

        orderSetting.setReservations(orderSetting.getReservations()+1);
        orderSettingDao.editReservationsByOrderDate(orderSetting);
        return new Result(true,MessageConstant.ORDER_SUCCESS,order.getId());
    }

    @Override
    public Map findById(Integer id) throws  Exception {
        Map map = orderDao.findById4Detail(id);
        if(map!=null){
            Date orderDate = (Date) map.get("orderDate");
            map.put("orderDate",DateUtils.parseDate2String(orderDate));
        }
        return map;
    }
}
