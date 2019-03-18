package com.seeyoo.zm.visit.repository;

import org.fage.vo.VisitStatisBean;
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
//    @Query(value = "select DATE_FORMAT(time,'%Y-%m-%d') as visitDate,count(*) as visitCount from visit_record group by DATE_FORMAT(time,'%Y-%m-%d')",nativeQuery = true)
//    public List<Object[]> visitCount();

    public List<VisitRecord> findAllByTimeBetween(@Param("start") Timestamp start,@Param("end") Timestamp end);

    Page<VisitRecord> findAll(Specification<VisitRecord> spec, Pageable pageable);

    @Query(nativeQuery = true, value = "SELECT new org.fage.vo.VisitStatisBean(date(dday) visitDate, count(*) - 1 as visitCount) FROM ( SELECT datelist as dday FROM calendar  UNION ALL SELECT time FROM visit_record) a where a.dday>=:startDate and a.dday<=:endDate GROUP BY visitDate /* #pageable# */",
        countQuery = "select count(*) from calendar where datelist>=:startDate and datelist<=:endDate")
    Page<VisitStatisBean> findAllByPageable(@Param("startDate") String startDate,@Param("endDate") String endDate,Pageable pageable);


//    @Query(value="select * from Category where parentid =?1 order by cid desc /* #pageable# */ ",countQuery="select count(*) from Category where parentid = ?1",nativeQuery = true)
//    Page<Category> findByParentid(int parentid, Pageable pageable);
//
//    @Query(value = "select new org.fage.vo.UserOutputVO(u.name, u.email, d.name as departmentName, count(r.id) as roleNum) from User u "
//            + "left join u.department d left join u.roles r group by u.id")
//    Page<UserOutputVO> findUserOutputVOAllPage(Pageable pageable);
}
