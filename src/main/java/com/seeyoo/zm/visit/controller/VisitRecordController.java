package com.seeyoo.zm.visit.controller;

import com.seeyoo.zm.visit.bean.VisitRecordBean;
import com.seeyoo.zm.visit.service.IncomeService;
import com.seeyoo.zm.visit.util.PageParam;
import com.seeyoo.zm.visit.bean.VisitStatisBean;
import com.seeyoo.zm.visit.model.VisitRecord;
import com.seeyoo.zm.visit.service.VisitRecordService;
import com.seeyoo.zm.visit.util.StringTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/record")
@Api(value = "探测信息相关的api", tags = {"访客记录信息接口"})
public class VisitRecordController {
    @Autowired
    private VisitRecordService visitRecordService;

    @Autowired
    private IncomeService incomeService;

    @Value("${vdb.vaildAdb}")
    private int Adb;

    @Value("${vdb.vaildBdb}")
    private int Bdb;

    @Value("${vdb.vaildPadb}")
    private int pAdb;

    @Value("${vdb.vaildPbdb}")
    private int pBdb;

    private static DecimalFormat df = new DecimalFormat("0.00");

    @RequestMapping(value = "/getVisitStastic", method = RequestMethod.GET)
    @ApiOperation(value = "客流详情")
    public Map<String, Object> getVisitStastic(String startDate, String endDate, @RequestParam(name = "page",defaultValue = "1") int page, @RequestParam(name = "size",defaultValue = "10") int size) {
        Map<String, Object> map = new HashMap<String, Object>();

        if (StringTools.isEmptyString(startDate)) {
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, -6);
            startDate = StringTools.dateToString(calendar.getTime());
        }
        if (StringTools.isEmptyString(endDate)) {
            Calendar calendar = Calendar.getInstance();
            endDate = StringTools.dateToString(calendar.getTime());
        }
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.setTime(StringTools.stringToDate(startDate));
        Calendar endCalendar = Calendar.getInstance();
        endCalendar.setTime(StringTools.stringToDate(endDate));
        Calendar tCalendar = Calendar.getInstance();
        tCalendar.setTime(startCalendar.getTime());
        List<VisitRecordBean> visitRecordBeans = new ArrayList<VisitRecordBean>();
        PageParam pageParam = new PageParam();
        pageParam.setStart(0);
        pageParam.setLength(5);
        pageParam.setLength(5);
        System.out.println("sssssssssssssss");
        Page<VisitStatisBean> pages = incomeService.findIncomeDailysByPage(pageParam,Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"));
        map.put("list", pages);
        return map;
    }

    public VisitRecordBean getVisitStastic(List<VisitRecord> list, String date, int aDb, int bDb, int pAdb, int pBdb) {
        if (list.size() <= 0) {
            return null;
        }
        int visitCount = 0;
        int vaildCount = 0;
        int passCount = 0;
        for (VisitRecord visitRecord : list) {
            if (date.equals(StringTools.timeStapm2Str(visitRecord.getTime()))) {

                visitCount++;
                if (Math.abs(visitRecord.getDb()) > aDb && Math.abs(visitRecord.getDb()) < bDb) {
                    vaildCount++;
                }
                if (Math.abs(visitRecord.getDb()) > pAdb && Math.abs(visitRecord.getDb()) < pBdb) {
                    passCount++;
                }
            }
        }
        VisitRecordBean visitRecordBean = new VisitRecordBean();
        visitRecordBean.setVisitCount(visitCount);
        visitRecordBean.setVaildCount(vaildCount);
        visitRecordBean.setPassCount(passCount);
        visitRecordBean.setVaildRate(visitCount > 0 ? df.format((float) vaildCount / visitCount) : "0");
        return visitRecordBean;
    }
}