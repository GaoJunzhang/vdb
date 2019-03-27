package com.seeyoo.zm.visit.service;

import com.seeyoo.zm.visit.bean.AssetsBean;
import com.seeyoo.zm.visit.bean.VisitCameraBean;
import com.seeyoo.zm.visit.model.VisitMember;
import com.seeyoo.zm.visit.repository.VisitMemberRepository;
import com.seeyoo.zm.visit.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class VisitMemberService {
    @Autowired
    private VisitMemberRepository visitMemberRepository;

    public VisitMember saveVisitMember(Long id, Integer age, short gender, Integer beauty, Integer stay, Timestamp stamp, int assetsId, Timestamp time){
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
        member.setAssetsId(assetsId);
        member.setTime(time);
        return visitMemberRepository.save(member);
    }

    public List<VisitCameraBean> dayVisitCount(Timestamp startDate, Timestamp endDate,int assetsId) {
        List<Object[]> list = visitMemberRepository.dayVisitCount(startDate, endDate,assetsId);
        return list.size() > 0 ? EntityUtils.castEntity(list, VisitCameraBean.class, new VisitCameraBean()) : null;
    }

    public int countByAgeBetweenAndStampBetween(Timestamp start,Timestamp end,int startAge,int endAge,int assetsId){
        return visitMemberRepository.countByAgeBetweenAndStampBetween(start,end,startAge,endAge,assetsId);
    }
    public List<AssetsBean> top10MemberAssets(Timestamp start,Timestamp end){
        List<Object[]> list = visitMemberRepository.top10MemberAssets(start, end);
        return list.size() > 0 ? EntityUtils.castEntity(list, AssetsBean.class, new AssetsBean()) : null;
    }
}