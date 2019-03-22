package com.seeyoo.zm.visit.controller;

import com.seeyoo.zm.visit.service.IncomeService;
import com.seeyoo.zm.visit.service.RegularCustomersService;
import com.seeyoo.zm.visit.service.VisitRecordService;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.HashMap;
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
    @ApiOperation(value = "terController",notes = "It create for myself testing")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "startDate",value = "startDate",dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "endDate",value = "startDate",dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "assetsId",value = "startDate",dataType = "Integer"),
            @ApiImplicitParam(paramType = "query",name = "page",value = "page",dataType = "Integer"),
            @ApiImplicitParam(paramType = "query",name = "rows",value = "rows",dataType = "Integer"),
    })
    public Map<String, Object> test() {
        Map<String, Object> map = new HashMap<String, Object>();
//        List<VisitRecord> visitRecords =  visitRecordService.findDistinctByMacAndTime("2019-03-15");
//        int data = visitRecordService.residenceTime("2019-03-18","00:BB:C1:D8:29:3E");
//        System.out.println(data/60);
        Timestamp timestamp = Timestamp.valueOf("2019-03-16 11:55:00");
        Timestamp timestamp2 = Timestamp.valueOf("2019-03-15 00:00:00");
        map.put("diff",(timestamp.getTime()-timestamp2.getTime())/3600000);
//        map.put("list",list);
        return map;
    }
}
