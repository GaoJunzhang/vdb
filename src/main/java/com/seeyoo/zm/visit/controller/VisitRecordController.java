package com.seeyoo.zm.visit.controller;

import com.seeyoo.zm.visit.bean.*;
import com.seeyoo.zm.visit.model.RegularCustomers;
import com.seeyoo.zm.visit.model.VisitRecord;
import com.seeyoo.zm.visit.service.IncomeService;
import com.seeyoo.zm.visit.service.RegularCustomersService;
import com.seeyoo.zm.visit.service.VisitRecordService;
import com.seeyoo.zm.visit.util.PageParam;
import com.seeyoo.zm.visit.util.StringTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
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

    @Autowired
    private RegularCustomersService regularCustomersService;

    @Value("${vdb.vaildAdb}")
    private int Adb;

    @Value("${vdb.vaildBdb}")
    private int Bdb;

    @Value("${vdb.vaildPadb}")
    private int pAdb;

    @Value("${vdb.vaildPbdb}")
    private int pBdb;

    @Value("${vdb.residence.level}")
    private int rLevel;

    @Value("${vdb.residence.mlevel}")
    private int mLevel;

    private static DecimalFormat df = new DecimalFormat("0.00");

    @RequestMapping(value = "/getVisitStastic", method = RequestMethod.GET)
    @ApiOperation(value = "客流详情")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "assetsId", value = "探针mac", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页第几页", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "分页页显示数量", dataType = "Integer"),
    })
    public Map<String, Object> getVisitStastic(String startDate, String endDate, Integer assetsId, @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "rows", defaultValue = "10") int rows) {
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
        List<VisitRecord> btVisits = visitRecordService.findAllByTimeBetween(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"), assetsId);
        PageParam pageParam = new PageParam(page, rows);
        Map<String, Object> map1 = incomeService.findIncomeDailysByPage(pageParam, Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"), assetsId);
        Page<VisitStatisBean> pages = (Page<VisitStatisBean>) map1.get("page");
        List<VisitStatisBean> visitStatisBeans = pages.getContent();
        for (VisitStatisBean visitStatisBean : visitStatisBeans) {
            VisitRecordBean visitRecordBean = getVisitStastic(btVisits, StringTools.dateToString(visitStatisBean.getVisitDate()), Adb, Bdb, pAdb, pBdb);
            visitRecordBean.setVisitDate(StringTools.dateToString(visitStatisBean.getVisitDate()));
            visitRecordBeans.add(visitRecordBean);
        }
        map.put("rows", visitRecordBeans);
        map.put("total", map1.get("total"));
//        map.put("page",pages.getTotalPages());
        return map;
    }

    public VisitRecordBean getVisitStastic(List<VisitRecord> list, String date, int aDb, int bDb, int pAdb, int pBdb) {
        VisitRecordBean visitRecordBean = new VisitRecordBean();
        if (list.size() <= 0) {
            return visitRecordBean;
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

        visitRecordBean.setVisitCount(visitCount);
        visitRecordBean.setVaildCount(vaildCount);
        visitRecordBean.setPassCount(passCount);
        visitRecordBean.setVaildRate(visitCount > 0 ? df.format((float) vaildCount * 100 / visitCount) + "%" : "0.00%");
        return visitRecordBean;
    }

    @RequestMapping(value = "/getVisitResidenceStastic", method = RequestMethod.GET)
    @ApiOperation(value = "驻留时长")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "assetsId", value = "探针mac", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页第几页", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "分页页显示数量", dataType = "Integer"),
    })
    public Map<String, Object> getVisitResidenceStastic(String startDate, String endDate, Integer assetsId, @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "rows", defaultValue = "10") int rows) {
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
        PageParam pageParam = new PageParam(page, rows);
        Map<String, Object> map1 = incomeService.findIncomeDailysByPage(pageParam, Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"), assetsId);
        Page<VisitStatisBean> pages = (Page<VisitStatisBean>) map1.get("page");
        List<VisitStatisBean> visitStatisBeans = pages.getContent();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>(pages.getSize());
        for (VisitStatisBean visitStatisBean : visitStatisBeans) {
//            根据日期查询出当天所有人员访问记录
            int allMin = 0;
            int rCount = 0;
            int mCount = 0;//深度访问数量
            Map<String, String> map2 = new HashMap<String, String>();
            List<VisitTimeBean> visitRecords = visitRecordService.findDistinctByMacAndTime(StringTools.dateToString(visitStatisBean.getVisitDate()), assetsId);
            int allcount = 0;
            if (visitRecords != null) {

                for (VisitTimeBean visitTimeBean : visitRecords) {
                    int rMin = visitTimeBean.getVisitTime().intValue()>0?visitTimeBean.getVisitTime().intValue() / 60:1;
                    allcount += visitTimeBean.getVisitCount().intValue();
                    allMin += rMin;
                    if (rMin > rLevel) {
                        rCount++;
                    }
                    if (rMin > mLevel) {
                        mCount++;
                    }
                }
            }
            map2.put("allVisitCount", allcount > 0 ? allMin / allcount + "" : "0");
            map2.put("residenceCount", rCount + "");//天-长访数量
            map2.put("residenceRate", allcount > 0 ? df.format((float) rCount * 100 / allcount) + "" : "0");//天-长访率
            map2.put("shortVisitCount", allcount - rCount + "");//端访数量=总数量-长访数量
            map2.put("shortVisitRate", allcount > 0 ? df.format((float) (allcount - rCount) * 100 / allcount) + "" : "0");//短访率
            map2.put("visitDate", visitStatisBean.getVisitDate() + "");//日期
            map2.put("residenceTime", allMin + "");//天-长访总时间
            map2.put("averageTime", allcount > 0 ? allMin / allcount + "" : "0");
            map2.put("mCount", mCount + "");
            list.add(map2);
        }
        map.put("rows", list);
        map.put("total", map1.get("total"));
        return map;
    }

    @RequestMapping(value = "/getNewAndOldCustomers", method = RequestMethod.GET)
    @ApiOperation(value = "新老客户")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "startDate", value = "开始日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "结束日期", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "assetsId", value = "探针mac", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "page", value = "分页第几页", dataType = "Integer"),
            @ApiImplicitParam(paramType = "query", name = "rows", value = "分页页显示数量", dataType = "Integer"),
    })
    public Map<String, Object> getNewAndOldCustomers(String startDate, String endDate, Integer assetsId, @RequestParam(name = "page", defaultValue = "1") int page, @RequestParam(name = "rows", defaultValue = "10") int rows) {
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
        PageParam pageParam = new PageParam(page, rows);
        Map<String, Object> map1 = incomeService.findIncomeDailysByPage(pageParam, Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"), assetsId);
        Page<VisitStatisBean> pages = (Page<VisitStatisBean>) map1.get("page");
        List<VisitStatisBean> visitStatisBeans = pages.getContent();
        List<RegularCustomers> regularCustomers = regularCustomersService.regularCustomers();
        List<Map<String, String>> list = new ArrayList<Map<String, String>>(pages.getSize());
        for (VisitStatisBean visitStatisBean : visitStatisBeans) {
            Map<String, String> map2 = new HashMap<String, String>();
            int oldCount = 0;
            int allCount = Integer.parseInt(visitStatisBean.getVisitCount() + "");
            List<DayVisitBean> dayVisitBeans = visitRecordService.dayVisits(visitStatisBean.getVisitDate() + "", assetsId);
            if (dayVisitBeans != null) {

                for (DayVisitBean dayVisitBean : dayVisitBeans) {
                    if (isOldCustomer(regularCustomers, dayVisitBean.getMac(), dayVisitBean.getTime())) {
                        oldCount += Integer.parseInt(dayVisitBean.getVisitDayCount() + "");
                    }
                }
            }
            map2.put("visitDate", visitStatisBean.getVisitDate() + "");//日期
            map2.put("newCustomerRate", allCount > 0 ? df.format((float) (allCount - oldCount) * 100 / allCount) : "0");//新客比率
            map2.put("newCustomerCount", allCount - oldCount + "");//新客数量
            map2.put("oldCustomerRate", allCount > 0 ? df.format((float) oldCount * 100 / allCount) + "" : "0");
            map2.put("oldCustomerCount", oldCount + "");
            list.add(map2);
        }
        map.put("rows", list);
        map.put("total", map1.get("total"));
        return map;
    }

    public boolean isOldCustomer(List<RegularCustomers> list, String mac, Timestamp time) {
        if (list.size() <= 0) {
            return false;
        }
        boolean flag = false;
        for (RegularCustomers regularCustomers : list) {
            long diffTime = (time.getTime() - regularCustomers.getCreateTime().getTime()) / 3600000;
            if (regularCustomers.getMac().equals(mac) && diffTime >= 12) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    @RequestMapping(value = "/getAllVisitStatis", method = RequestMethod.GET)
    @ApiOperation(value = "Visit General statistics ")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "startDate", value = "startDate", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "endDate", dataType = "String"),
    })
    public Map<String, Object> getAllVisitStatis(String startDate, String endDate) {
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
//        List<VisitRecord> visitRecords = visitRecordService.findAllByTimeBetween(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"), null);
//        List<VisitRecord> visitRecords1 = visitRecordService.findAllByTimeBetweenAndDbBetween(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"), Adb, Bdb);
        List<VisitRecordBean> visitRecordBeans = new ArrayList<VisitRecordBean>();
        List<VisitStatisBean> visitStatisBeans = visitRecordService.dayVisitCount(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"));
        List<VisitStatisBean> visitVaildStatisBeans = visitRecordService.dayVisitVaildCount(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"), Adb, Bdb);
        while (startCalendar.compareTo(endCalendar) <= 0) {
            VisitRecordBean visitRecordBean = new VisitRecordBean();
            int dayCount = 0;
            int dayVaildCount = 0;
            for (VisitStatisBean visitStatisBean : visitStatisBeans) {
                if (visitStatisBean.getVisitDate().equals(startCalendar.getTime())) {
                    dayCount += visitStatisBean.getVisitCount().intValue();
                }
            }
            for (VisitStatisBean visitStatisBean : visitVaildStatisBeans) {
                if (visitStatisBean.getVisitDate().equals(startCalendar.getTime())) {
                    dayVaildCount += visitStatisBean.getVisitCount().intValue();
                }
            }
            visitRecordBean.setVisitDate(StringTools.dateToString(startCalendar.getTime()));
            visitRecordBean.setVisitCount(dayCount);
            visitRecordBean.setVaildCount(dayVaildCount);
            visitRecordBeans.add(visitRecordBean);
            startCalendar.add(Calendar.DATE, 1);
        }
        int visitAllTime = visitRecordService.getAllVisitTime(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"));//所有访问时间
        List<RegularCustomers> regularCustomers = regularCustomersService.regularCustomers();//全部老客户
        List<DayVisitBean> allCustomer = visitRecordService.dayVisiters(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"));
        int oldCustomer = 0;
        if (allCustomer!=null){
            for (DayVisitBean dayVisitBean:allCustomer){
                if (isOldCustomer(regularCustomers,dayVisitBean.getMac(),dayVisitBean.getTime())){
                    oldCustomer++;
                }
            }
        }
        map.put("visitAllTime",visitAllTime);
        map.put("list", visitRecordBeans);
        map.put("oldCustomer",oldCustomer);
        return map;
    }

    @RequestMapping(value = "/getTop10Assets", method = RequestMethod.GET)
    @ApiOperation(value = "Assets Top10")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "startDate", value = "startDate", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "endDate", dataType = "String"),
    })
    public Map<String, Object> getTop10Assets(String startDate, String endDate,@RequestParam(name = "type",defaultValue = "0",required = false) int type) {
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
        List<AssetsBean> assetsBeans = null;
        if (type==1){
            assetsBeans = visitRecordService.top10Assets(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"));
        }else {
            assetsBeans = visitRecordService.top10VaildAssets(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"),Adb,Bdb);
        }
        map.put("rows",assetsBeans);
        map.put("total",assetsBeans.size());
        return map;
    }
}
