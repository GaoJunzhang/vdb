package com.seeyoo.zm.visit.repository;

import com.seeyoo.zm.visit.model.VisitRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.List;

public interface VisitRecordRepository extends JpaRepository<VisitRecord,Long> {


    public List<VisitRecord> findAllByTimeBetweenAndAssetsId(@Param("start") Timestamp start,@Param("end") Timestamp end ,@Param("assetsId") int assetsId);

    public List<VisitRecord> findAllByTimeBetween(@Param("start") Timestamp start,@Param("end") Timestamp end);

    @Query(nativeQuery = true,value = "SELECT ROUND(UNIX_TIMESTAMP(MAX(t.time)) - UNIX_TIMESTAMP(MIN(t.time))) as visitTime,t.mac,COUNT(t.id) as visitCount FROM visit_record t where DATE_FORMAT(t.time,'%Y-%m-%d')=:time and t.assets_id=:assetsId group  by t.mac")
    List<Object[]> findByTime(@Param("time") String time,@Param("assetsId") int assetsId);

    @Query(nativeQuery = true,value = "SELECT ROUND(UNIX_TIMESTAMP(MAX(t.time)) - UNIX_TIMESTAMP(MIN(t.time))) from visit_record t where t.mac=:mac and DATE_FORMAT(t.time,'%Y-%m-%d')=:time")
    public int residenceTime(@Param("time") String time,@Param("mac") String mac);

    @Query(nativeQuery = true,value = "SELECT count(id) as visitDayCount,t.mac,t.time FROM visit_record t where DATE_FORMAT(t.time,'%Y-%m-%d')=:time and assets_id=:assetsId GROUP BY t.mac")
    public List<Object[]> dayVisits(@Param("time") String time,@Param("assetsId") int assetsId);

//
//    @Query(nativeQuery = true,value = "SELECT * from visit_record t where t.time>=:start and t.time<=:end and ABS(t.db)>=:sdb and ABS(t.db)<=:edb")
//    public List<VisitRecord> findAllByTimeBetweenAndDbBetween(@Param("start") Timestamp start,@Param("end") Timestamp end,@Param("sdb") int sdb,@Param("edb") int edb);

    @Query(nativeQuery = true,value = "SELECT date(t.time) as visitDate ,count(id) as visitCount from visit_record t where  (t.time between :start and :end) GROUP BY DATE_FORMAT(t.time,'%Y-%m-%d')")
    public List<Object[]> dayVisitCount(@Param("start") Timestamp start,@Param("end") Timestamp end);

    @Query(nativeQuery = true,value = "SELECT date(t.time) as visitDate ,count(id) as visitCount from visit_record t where(ABS(t.db) BETWEEN :sdb and :edb) and  (t.time between :start and :end) GROUP BY DATE_FORMAT(t.time,'%Y-%m-%d')")
    public List<Object[]> dayVisitVaildCount(@Param("start") Timestamp start,@Param("end") Timestamp end,@Param("sdb") int sdb,@Param("edb") int edb);

    @Query(nativeQuery = true,value = "SELECT ROUND(UNIX_TIMESTAMP(MAX(t.time)) - UNIX_TIMESTAMP(MIN(t.time)))  from visit_record t where (t.time between :start and :end) GROUP BY t.mac;")
    public List<BigInteger> countResidenceTime(@Param("start") Timestamp start, @Param("end") Timestamp end);

    @Query(nativeQuery = true,value = "SELECT count(id) as visitDayCount,t.mac,t.time FROM visit_record t where (t.time between :start and :end) GROUP BY t.mac")
    public List<Object[]> dayVisiters(@Param("start") Timestamp start,@Param("end") Timestamp end);

    @Query(nativeQuery = true,value = "SELECT t1.name,avCount from(SELECT COUNT(id) as avcount,assets_id from visit_record t where (t.time between :start and :end) GROUP BY t.assets_id) a LEFT JOIN assets t1 on a.assets_id=t1.id order BY a.avcount desc LIMIT 0,10")
    public List<Object[]> top10Assets(@Param("start") Timestamp start,@Param("end") Timestamp end);

    @Query(nativeQuery = true,value = "SELECT t1.name,avCount from(SELECT COUNT(id) as avcount,assets_id from visit_record t where (t.time between :start and :end) AND(ABS(t.db) BETWEEN :sdb AND :edb) GROUP BY t.assets_id) a LEFT JOIN assets t1 on a.assets_id=t1.id order BY a.avcount desc LIMIT 0,10")
    public List<Object[]> top10VaildAssets(@Param("start") Timestamp start,@Param("end") Timestamp end,@Param("sdb") int sdb,@Param("edb") int edb);
}
