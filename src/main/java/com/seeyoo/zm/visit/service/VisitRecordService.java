package com.seeyoo.zm.visit.service;

import com.seeyoo.zm.visit.bean.AssetsBean;
import com.seeyoo.zm.visit.bean.DayVisitBean;
import com.seeyoo.zm.visit.bean.VisitStatisBean;
import com.seeyoo.zm.visit.bean.VisitTimeBean;
import com.seeyoo.zm.visit.model.VisitRecord;
import com.seeyoo.zm.visit.repository.VisitRecordRepository;
import com.seeyoo.zm.visit.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
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

    public List<VisitRecord> findAllByTimeBetween(Timestamp start, Timestamp end, Integer assetsId) {
        if (assetsId == null) {
            return visitRecordRepository.findAllByTimeBetween(start, end);
        }
        return visitRecordRepository.findAllByTimeBetweenAndAssetsId(start, end, assetsId);
    }

    public List<VisitTimeBean> findDistinctByMacAndTime(String time, int assetsId) {
        List<Object[]> list = visitRecordRepository.findByTime(time, assetsId);
        return list.size() > 0 ? EntityUtils.castEntity(list, VisitTimeBean.class, new VisitTimeBean()) : null;
    }

    public int residenceTime(String time, String mac) {
        return visitRecordRepository.residenceTime(time, mac);
    }

    public List<DayVisitBean> dayVisits(String time, int assetsId) {
        List<Object[]> list = visitRecordRepository.dayVisits(time, assetsId);
        return list.size() > 0 ? EntityUtils.castEntity(list, DayVisitBean.class, new DayVisitBean()) : null;
    }

    public List<VisitStatisBean> dayVisitCount(Timestamp startDate, Timestamp endDate) {
        List<Object[]> list = visitRecordRepository.dayVisitCount(startDate, endDate);
        return list.size() > 0 ? EntityUtils.castEntity(list, VisitStatisBean.class, new VisitStatisBean()) : null;
    }

    public List<VisitStatisBean> dayVisitVaildCount(Timestamp startDate, Timestamp endDate,int sdb,int edb) {
        List<Object[]> list = visitRecordRepository.dayVisitVaildCount(startDate, endDate,sdb,edb);
        return list.size() > 0 ? EntityUtils.castEntity(list, VisitStatisBean.class, new VisitStatisBean()) : null;
    }

    public int getAllVisitTime(Timestamp startDate,Timestamp endDate){
        List<BigInteger> list = visitRecordRepository.countResidenceTime(startDate,endDate);
        int count = 0;
        for (int i=0;i<list.size();i++){
            if (list.get(i).intValue()==0){
                count+=1;
            }else {
                count+=list.get(i).intValue()/60;
            }
        }
        return count;
    }

    public List<DayVisitBean> dayVisiters(Timestamp startDate,Timestamp endDate) {
        List<Object[]> list = visitRecordRepository.dayVisiters(startDate, endDate);
        return list.size() > 0 ? EntityUtils.castEntity(list, DayVisitBean.class, new DayVisitBean()) : null;
    }

    public List<AssetsBean> top10Assets(Timestamp startDate,Timestamp endDate) {
        List<Object[]> list = visitRecordRepository.top10Assets(startDate, endDate);
        return list.size() > 0 ? EntityUtils.castEntity(list, AssetsBean.class, new AssetsBean()) : null;
    }
    public List<AssetsBean> top10VaildAssets(Timestamp startDate,Timestamp endDate,int sdb,int edb) {
        List<Object[]> list = visitRecordRepository.top10VaildAssets(startDate, endDate,sdb,edb);
        return list.size() > 0 ? EntityUtils.castEntity(list, AssetsBean.class, new AssetsBean()) : null;
    }
}
