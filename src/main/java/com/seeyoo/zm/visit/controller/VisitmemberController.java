package com.seeyoo.zm.visit.controller;

import com.seeyoo.zm.visit.bean.AssetsBean;
import com.seeyoo.zm.visit.bean.VisitCameraBean;
import com.seeyoo.zm.visit.service.VisitMemberService;
import com.seeyoo.zm.visit.util.StringTools;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.*;

@RestController
@RequestMapping("/camera")
@Api(value = "摄像头统计接口", tags = {"camera info"})
public class VisitmemberController {
    @Autowired
    private VisitMemberService visitMemberService;


    @RequestMapping(value = "/getCameraStatis", method = RequestMethod.GET)
    @ApiOperation(value = "Visit General statistics ")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "startDate", value = "startDate", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "endDate", dataType = "String"),
    })
    public Map<String, Object> getCameraStatis(String startDate, String endDate,int assetsId) {
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
        List<VisitCameraBean> visitCameraBeans = visitMemberService.dayVisitCount(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"),assetsId);
        int youngCount = visitMemberService.countByAgeBetweenAndStampBetween(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"),0,18,assetsId);
        int bigYoungCount = visitMemberService.countByAgeBetweenAndStampBetween(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"),19,35,assetsId);
        int midlifecount = visitMemberService.countByAgeBetweenAndStampBetween(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"),36,60,assetsId);
        int oldAgeCount = visitMemberService.countByAgeBetweenAndStampBetween(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"),61,200,assetsId);
        int betweenDays = (int)((endCalendar.getTime().getTime() - startCalendar.getTime().getTime()) / (1000 * 60 * 60 *24));
        int[] maleArr = new int[betweenDays+1];
        int[] femaleArr = new int[betweenDays+1];
        String[] dateArry = new String[betweenDays+1];
        int[] allCount = new int[betweenDays+1];
        int[] allStay = new int[betweenDays+1];
        int n = 0;
        int sumMaleCount = 0;
        int sumFemaleCount = 0;
        int sumVisitCount = 0;
        int sumStayCount = 0;
        while (startCalendar.compareTo(endCalendar) <= 0) {
            int dayCount = 0;//天总次数
            int dayStayCount = 0;//每天停留时间
            int maleCount = 0;//每天男性次数
            int femaleCount =0;//每天女性次数
            if (visitCameraBeans != null) {
                for (VisitCameraBean visitCameraBean : visitCameraBeans) {
                    if (visitCameraBean.getVisitDate().equals(startCalendar.getTime())) {
                        dayCount += visitCameraBean.getVisitCount().intValue();
                        dayStayCount += visitCameraBean.getStayCount().intValue();
                        if (visitCameraBean.getGender().intValue()==0){//0女，1男
                            femaleCount+=visitCameraBean.getVisitCount().intValue();
                        }else{
                            maleCount+=visitCameraBean.getVisitCount().intValue();
                        }
                    }
                }
            }
            sumMaleCount += maleCount;
            sumFemaleCount += femaleCount;
            maleArr[n] = maleCount;
            femaleArr[n] = femaleCount;
            dateArry[n] = StringTools.dateToString(startCalendar.getTime());
            allCount[n] = dayCount;
            allStay[n] = dayStayCount;
            sumVisitCount += dayCount;
            sumStayCount += dayStayCount;
            n++;
            startCalendar.add(Calendar.DATE, 1);
        }
        map.put("femaleArr",femaleArr);
        map.put("maleArr",maleArr);
        map.put("dateArry",dateArry);
        map.put("allCount",allCount);
        map.put("allStay",allStay);
        map.put("sumFemaleCount",sumFemaleCount);
        map.put("sumMaleCount",sumMaleCount);
        map.put("sumStayCount",sumStayCount);
        map.put("sumVisitCount",sumVisitCount);

        //年龄段统计
        map.put("youngCount",youngCount);
        map.put("bigYoungCount",bigYoungCount);
        map.put("midlifecount",midlifecount);
        map.put("oldAgeCount",oldAgeCount);
        return map;
    }

    @RequestMapping(value = "/top10MemberAssets", method = RequestMethod.GET)
    @ApiOperation(value = "Assets Member Top10")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query", name = "startDate", value = "startDate", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "endDate", value = "endDate", dataType = "String"),
            @ApiImplicitParam(paramType = "query", name = "type", value = "0 or 1", dataType = "int"),
    })
    public Map<String,Object> top10MemberAssets(String startDate, String endDate){
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
        List<AssetsBean> assetsBeans = visitMemberService.top10MemberAssets(Timestamp.valueOf(startDate + " 00:00:00"), Timestamp.valueOf(endDate + " 23:59:59"));
        map.put("rows",assetsBeans);
        map.put("total",assetsBeans.size());
        return map;
    }
}
