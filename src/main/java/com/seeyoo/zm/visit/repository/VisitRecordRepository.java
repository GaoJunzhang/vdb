package com.seeyoo.zm.visit.repository;

import com.seeyoo.zm.visit.model.VisitRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface VisitRecordRepository extends JpaRepository<VisitRecord,Long> {


    public List<VisitRecord> findAllByTimeBetween(@Param("start") Timestamp start,@Param("end") Timestamp end);

    Page<VisitRecord> findAll(Specification<VisitRecord> spec, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT date(dday) visitDate, count(*) - 1 as visitCount FROM ( SELECT datelist as dday FROM calendar  UNION ALL SELECT time FROM visit_record) a where a.dday>=:startDate and a.dday<=:endDate GROUP BY visitDate /* #pageable# */",
        countQuery = "select count(*) from Calendar where datelist>=:startDate and datelist<=:endDate")
    Page<Object[]> findAllByPageable(@Param("startDate") String startDate,@Param("endDate") String endDate,Pageable pageable);

}
