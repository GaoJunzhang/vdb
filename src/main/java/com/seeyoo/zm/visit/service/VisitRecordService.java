package com.seeyoo.zm.visit.service;

import com.seeyoo.zm.visit.bean.DayVisitBean;
import com.seeyoo.zm.visit.bean.VisitTimeBean;
import com.seeyoo.zm.visit.model.VisitRecord;
import com.seeyoo.zm.visit.repository.VisitRecordRepository;
import com.seeyoo.zm.visit.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class VisitRecordService {
    @Autowired
    private VisitRecordRepository visitRecordRepository;

    public VisitRecord saveVisitRecord(Long id, String mac, Timestamp time, Integer db, Integer aId) {
        VisitRecord visitRecord = null;
        if (id == null) {
            visitRecord = new VisitRecord();
        } else {
            visitRecord = visitRecordRepository.findOne(id);
        }
        visitRecord.setDb(db);
        visitRecord.setMac(mac);
        visitRecord.setTime(time);
        visitRecord.setAssetsId(aId);
        return visitRecordRepository.save(visitRecord);
    }

    public List<VisitRecord> findAllByTimeBetween(Timestamp start, Timestamp end,Integer assetsId){
        if (assetsId==null){
            return visitRecordRepository.findAllByTimeBetween(start,end);
        }
        return visitRecordRepository.findAllByTimeBetweenAndAssetsId(start,end,assetsId);
    }
    public List<VisitTimeBean> findDistinctByMacAndTime(String time){
        List<Object[]> list = visitRecordRepository.findByTime(time);
        return list.size()>0?EntityUtils.castEntity(list,VisitTimeBean.class,new VisitTimeBean()):null;
    }

    public int residenceTime(String time,String mac){
        return visitRecordRepository.residenceTime(time,mac);
    }

    public List<DayVisitBean> dayVisits(String time){
        List<Object[]> list = visitRecordRepository.dayVisits(time);
        return list.size()>0?EntityUtils.castEntity(list,DayVisitBean.class,new DayVisitBean()):null;
    }
}
