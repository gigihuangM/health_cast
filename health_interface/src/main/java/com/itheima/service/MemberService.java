package com.itheima.service;

import com.itheima.pojo.Member;

import java.util.List;

public interface MemberService {
    Member findMemberByTelephone(String telephone);
    void  add(Member member);
    List<Integer> findMemberByMonths(List<String> months);
}
