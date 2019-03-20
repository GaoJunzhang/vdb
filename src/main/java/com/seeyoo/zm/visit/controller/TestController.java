package com.seeyoo.zm.visit.controller;

import com.seeyoo.zm.visit.bean.DayVisitBean;
import com.seeyoo.zm.visit.model.RegularCustomers;
import com.seeyoo.zm.visit.model.VisitRecord;
import com.seeyoo.zm.visit.service.IncomeService;
import com.seeyoo.zm.visit.service.RegularCustomersService;
import com.seeyoo.zm.visit.service.VisitRecordService;
import com.seeyoo.zm.visit.util.StringTools;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/test")
public class TestController {

    @Autowired
    private VisitRecordService visitRecordService;

    @Autowired
    private IncomeService incomeService;

    @Autowired
    private RegularCustomersService regularCustomersService;

    @RequestMapping(value = "/test", method = RequestMethod.GET)
    @ApiOperation(value = "客流详情")
    public Map<String, Object> test() {
        Map<String, Object> map = new HashMap<String, Object>();
//        List<VisitRecord> visitRecords =  visitRecordService.findDistinctByMacAndTime("2019-03-15");
//        int data = visitRecordService.residenceTime("2019-03-18","00:BB:C1:D8:29:3E");
//        System.out.println(data/60);
        List<DayVisitBean> list =visitRecordService.dayVisits("2019-03-15");
        Timestamp timestamp = Timestamp.valueOf("2019-03-16 11:55:00");
        Timestamp timestamp2 = Timestamp.valueOf("2019-03-15 00:00:00");
        map.put("diff",(timestamp.getTime()-timestamp2.getTime())/3600000);
//        map.put("list",list);
        return map;
    }
}
