package com.seeyoo.zm.visit.repository;

import com.seeyoo.zm.visit.model.Assets;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AssetsRepository extends JpaRepository<Assets, Integer> {
    Page<Assets> findAll(Specification<Assets> spec, Pageable pageable);

    public Assets findByMac(@Param("mac") String mac);

    public List<Assets> findAllById(@Param("id") int id);

}
