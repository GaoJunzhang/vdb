package com.seeyoo.zm.visit.repository;

import com.seeyoo.zm.visit.model.RegularCustomers;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.sql.Timestamp;
import java.util.List;

public interface RegularCustomersRepository extends JpaRepository<RegularCustomers,Long> {
    public List<RegularCustomers> findByMacAndCreateTimeBefore(@Param("mac") String mac, @Param("createTime") Timestamp createTime);

    public List<RegularCustomers> findByMac(@Param("mac") String mac);

    public List<RegularCustomers> findAll();
}
