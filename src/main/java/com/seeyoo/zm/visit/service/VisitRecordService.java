package com.seeyoo.zm.visit.service;

import com.seeyoo.zm.visit.bean.VisitStatisBean;
import com.seeyoo.zm.visit.model.VisitRecord;
import com.seeyoo.zm.visit.repository.VisitRecordRepository;
import com.seeyoo.zm.visit.util.StringTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.ArrayList;
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

    public List<VisitRecord> findAllByTimeBetween(Timestamp start, Timestamp end,int assetsId){
        return visitRecordRepository.findAllByTimeBetweenAndAssetsId(start,end,assetsId);
    }
    public List<VisitRecord> findDistinctByMacAndTime(String time){
        return visitRecordRepository.findByTime(time);
    }

    public int residenceTime(String time,String mac){
        return visitRecordRepository.residenceTime(time,mac);
    }
}
