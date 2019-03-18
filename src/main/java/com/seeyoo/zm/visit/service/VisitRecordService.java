package com.seeyoo.zm.visit.service;

import org.fage.vo.VisitStatisBean;
import com.seeyoo.zm.visit.model.VisitRecord;
import com.seeyoo.zm.visit.repository.VisitRecordRepository;
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

//    public List<VisitRecordBean> visitCount() {
//        List<Object[]> list = visitRecordRepository.visitCount();
//        List<VisitRecordBean> visitRecordBeans = list.size() > 0 ? EntityUtils.castEntity(list, VisitRecordBean.class, new VisitRecordBean()) : null;
//        return visitRecordBeans;
//    }

    public List<VisitRecord> findAllByTimeBetween(Timestamp start, Timestamp end){
        return visitRecordRepository.findAllByTimeBetween(start,end);
    }

    public Page<VisitRecord> VisitRecords(Timestamp start,Timestamp end, int page, int size, String sortType, String sortValue) {
        String[] svs = sortValue.split(",");
        String[] sts = sortType.split(",");

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        for (int i = 0; i < svs.length; i++) {
            Sort.Order order = new Sort.Order(Sort.Direction.fromString(sts[i]), svs[i]);
            orders.add(order);
        }
        Sort sort = new Sort(orders);
        Pageable pageable = new PageRequest(page, size, sort);

        Specification<VisitRecord> specification = new Specification<VisitRecord>() {
            @Override
            public Predicate toPredicate(Root<VisitRecord> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
                predicate.getExpressions().add(cb.equal(root.get("isDelete"), (short) 0));
                if (start != null) {

                    predicate.getExpressions().add(cb.greaterThan(root.get("time"),start));
                }
                if (end != null) {

                    predicate.getExpressions().add(cb.lessThan(root.get("time"),end));
                }
                return predicate;
            }

        };

        return visitRecordRepository.findAll(specification, pageable);
    }


    public Page<VisitStatisBean> pageVisitStatis (String startDate,String endDate,int page,int size){
        Pageable pageable = new PageRequest(page, size);
        return visitRecordRepository.findAllByPageable(startDate,endDate,pageable);
    }
}
