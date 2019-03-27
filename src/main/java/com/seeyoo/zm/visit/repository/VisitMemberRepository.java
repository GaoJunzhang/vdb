package com.seeyoo.zm.visit.repository;

import com.seeyoo.zm.visit.model.VisitMember;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface VisitMemberRepository extends JpaRepository<VisitMember,Long> {
    @Query(nativeQuery = true,value = "SELECT date(t.time) as visitDate ,count(id) as visitCount,t.gender,SUM(t.stay) as stayCount from visit_member t where  (t.time between :start and :end) and t.assets_id=:assetsId GROUP BY DATE_FORMAT(t.stamp,'%Y-%m-%d'),t.gender")
    public List<Object[]> dayVisitCount(@Param("start") Timestamp start, @Param("end") Timestamp end,@Param("assetsId") int assetsId);

    @Query(nativeQuery = true,value = "SELECT count(t.id) from visit_member t where (t.age BETWEEN :startAge and :endAge) and (t.time BETWEEN :start AND :end) and t.assets_id=:assetsId")
    public int countByAgeBetweenAndStampBetween(@Param("start") Timestamp start, @Param("end") Timestamp end,@Param("startAge") int startAge, @Param("endAge") int endAge,@Param("assetsId") int assetsId);

    @Query(nativeQuery = true,value = "SELECT t1.name,avCount from(SELECT COUNT(id) as avcount,assets_id from visit_member t where (t.time between :start and :end) GROUP BY t.assets_id) a LEFT JOIN assets t1 on a.assets_id=t1.id order BY a.avcount desc LIMIT 0,10")
    public List<Object[]> top10MemberAssets(@Param("start") Timestamp start,@Param("end") Timestamp end);
}
