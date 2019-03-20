package com.seeyoo.zm.visit.service;

import com.seeyoo.zm.visit.model.RegularCustomers;
import com.seeyoo.zm.visit.repository.RegularCustomersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class RegularCustomersService {
    @Autowired
    private RegularCustomersRepository regularCustomersRepository;

    public List<RegularCustomers> findByMacAndCreateTimeBefore(String mac, Timestamp createTime){
        return regularCustomersRepository.findByMacAndCreateTimeBefore(mac,createTime);
    }

    public List<RegularCustomers> findByMac(String mac){
        return regularCustomersRepository.findByMac(mac);
    }

    public void saveRegularCutomers(Long id ,String mac,Timestamp cerateTime){
        RegularCustomers regularCustomers = null;
        if (id==null){
            regularCustomers = new RegularCustomers();
        }else {
            regularCustomers = regularCustomersRepository.findOne(id);
        }
        regularCustomers.setMac(mac);
        regularCustomers.setCreateTime(cerateTime);
        regularCustomersRepository.save(regularCustomers);
    }

    public List<RegularCustomers> regularCustomers(){
        return regularCustomersRepository.findAll();
    }
}
