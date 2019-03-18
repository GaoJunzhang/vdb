package com.seeyoo.zm.visit.service;

import com.seeyoo.zm.visit.model.VisitMember;
import com.seeyoo.zm.visit.repository.VisitMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class VisitMemberService {
    @Autowired
    private VisitMemberRepository visitMemberRepository;

    public VisitMember saveVisitMember(Long id,Integer age,short gender, Integer beauty,Integer stay,String stamp, String mac, Timestamp time){
        VisitMember member = null;
        if (id == null){
            member = new VisitMember();
        }else {
            member = visitMemberRepository.findOne(id);
        }
        member.setAge(age);
        member.setGender(gender);
        member.setBeauty(beauty);
        member.setStamp(stamp);
        member.setStay(stay);
        member.setMac(mac);
        member.setTime(time);
        return visitMemberRepository.save(member);
    }
}