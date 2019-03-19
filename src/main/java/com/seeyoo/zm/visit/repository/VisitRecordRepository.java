package com.seeyoo.zm.visit.repository;

import com.seeyoo.zm.visit.model.VisitRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import javax.xml.crypto.Data;
import java.sql.Timestamp;
import java.util.List;

public interface VisitRecordRepository extends JpaRepository<VisitRecord,Long> {


    public List<VisitRecord> findAllByTimeBetween(@Param("start") Timestamp start,@Param("end") Timestamp end);

    @Query(nativeQuery = true,value = "SELECT * FROM visit_record t where DATE_FORMAT(t.time,'%Y-%m-%d')=:time group  by t.mac")
    List<VisitRecord> findByTime(@Param("time") String time);

    @Query(nativeQuery = true,value = "SELECT ROUND(UNIX_TIMESTAMP(MAX(t.time)) - UNIX_TIMESTAMP(MIN(t.time))) from visit_record t where t.mac=:mac and DATE_FORMAT(t.time,'%Y-%m-%d')=:time")
    public int residenceTime(@Param("time") String time,@Param("mac") String mac);
}
