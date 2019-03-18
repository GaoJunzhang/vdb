package com.seeyoo.zm.visit.service;

import com.seeyoo.zm.visit.model.Assets;
import com.seeyoo.zm.visit.repository.AssetsRepository;
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
public class AssetsService {
    @Autowired
    private AssetsRepository assetsRepository;

    public Assets saveAssets(Integer id, Timestamp time, String mac) {
        Assets assets = null;
        if (id == null) {
            assets = new Assets();
            assets.setTime(new Timestamp(System.currentTimeMillis()));
        } else {
            assets = assetsRepository.findOne(id);
        }
        assets.setMac(mac);
//        assets.setTime(time);
        return assetsRepository.save(assets);
    }

    public Page<Assets> collages(String mac, int page, int size, String sortType, String sortValue) {
        String[] svs = sortValue.split(",");
        String[] sts = sortType.split(",");

        List<Sort.Order> orders = new ArrayList<Sort.Order>();
        for (int i = 0; i < svs.length; i++) {
            Sort.Order order = new Sort.Order(Sort.Direction.fromString(sts[i]), svs[i]);
            orders.add(order);
        }
        Sort sort = new Sort(orders);
        Pageable pageable = new PageRequest(page, size, sort);

        Specification<Assets> specification = new Specification<Assets>() {
            @Override
            public Predicate toPredicate(Root<Assets> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                Predicate predicate = cb.conjunction();
//                predicate.getExpressions().add(cb.equal(root.get("isDelete"), (short) 0));
                if (mac != null && !mac.trim().isEmpty()) {
                    predicate.getExpressions().add(cb.like(root.get("mac"), "%" + mac + "%"));
                }
                return predicate;
            }

        };

        return assetsRepository.findAll(specification, pageable);
    }

    public Assets findByMac(String mac){
        return assetsRepository.findByMac(mac);
    }
}