package com.seeyoo.zm.visit.controller;

import com.seeyoo.zm.visit.bean.VisitRecordBean;
import org.fage.vo.VisitStatisBean;
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

import java.text.DecimalFormat;
import java.util.*;

@RestController
@RequestMapping("/record")
@Api(value = "探测信息相关的api", tags = {"访客记录信息接口"})
public class VisitRecordController {
    @Autowired
    private VisitRecordService visitRecordService;

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
        Page<VisitStatisBean> pages = visitRecordService.pageVisitStatis(startDate,endDate,page,size);
//        List<VisitRecord> list = visitRecordService.findAllByTimeBetween(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"));
//        while (tCalendar.compareTo(endCalendar) <= 0) {
//            VisitRecordBean visitRecordBean = new VisitRecordBean();
//            String tString = StringTools.dateToString(tCalendar.getTime());
//            visitRecordBean = getVisitStastic(list,tString,Adb,Bdb,pAdb,pBdb);
//            visitRecordBean.setVisitDate(tString);
//            tCalendar.add(Calendar.DATE, 1);
//            visitRecordBeans.add(visitRecordBean);
//        }
//        PageHelper.startPage(1, 5);
//        PageInfo<VisitRecordBean> appsPageInfo = new PageInfo<>(visitRecordBeans);
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
