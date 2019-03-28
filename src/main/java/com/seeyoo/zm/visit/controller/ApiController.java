package com.seeyoo.zm.visit.controller;

import com.seeyoo.zm.visit.model.Assets;
import com.seeyoo.zm.visit.model.RegularCustomers;
import com.seeyoo.zm.visit.service.AssetsService;
import com.seeyoo.zm.visit.service.RegularCustomersService;
import com.seeyoo.zm.visit.service.VisitMemberService;
import com.seeyoo.zm.visit.service.VisitRecordService;
import com.seeyoo.zm.visit.result.ResultVO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping(value = "/api/web")
@Api(value = "探测信息相关的api",tags = {"访客和人员信息接口"})
public class ApiController {
    @Autowired
    private VisitRecordService visitRecordService;
    @Autowired
    private VisitMemberService visitMemberService;
    @Autowired
    private AssetsService assetsService;
    @Autowired
    private RegularCustomersService regularCustomersService;

    @ApiOperation(value="保存访客记录", notes="根据json对象保存")
    @ApiImplicitParam(name = "jsonObje", value = "JSON对象", required = true, dataType = "JsonObj", paramType = "path")
    @RequestMapping(value = "/postVisitorRecord", method = RequestMethod.POST,produces = "application/json")
    public Object postVisitorRecord(@RequestBody String arryObj) {
        JSONObject jsonObject = JSONObject.fromObject(arryObj);
        if (jsonObject == null || !jsonObject.containsKey("list")) {
            return ResultVO.getFailed("Request parameter no allow null!");
        }
        //请求终端mac
        if (!jsonObject.containsKey("mac")) {
            return ResultVO.getFailed("Terminal Mac Unavailable,Mac Field is Null!");
        }
        Assets assets = assetsService.findByMac(jsonObject.get("mac")+"");
        if (assets==null){
            assets = assetsService.saveAssets(null,  jsonObject.get("mac") + "",jsonObject.get("mac") + "",(short)0);
        }
        JSONArray list = JSONArray.fromObject(jsonObject.get("list"));
        Timestamp time1 = new Timestamp(System.currentTimeMillis());
        JSONObject obj = null;
        JSONObject macObj = null;
        try {
            if (list.size() > 0) {
                for (int i = 0; i < list.size(); i++) {
                    obj = JSONObject.fromObject(list.get(i));
                    time1 = Timestamp.valueOf(obj.get("time") + "");
                    if (obj.containsKey("macs")) {
                        JSONArray macsArry = JSONArray.fromObject(obj.get("macs"));
                        for (int j = 0; j < macsArry.size(); j++) {
                            macObj = JSONObject.fromObject(macsArry.get(j));
                            visitRecordService.saveVisitRecord(null, macObj.get("mac") + "", time1, Integer.parseInt(macObj.get("db") + ""), assets.getId());
                            List<RegularCustomers> regularCustomers = regularCustomersService.findByMac(macObj.get("mac")+"");
                            if (regularCustomers.size()<=0){
                                regularCustomersService.saveRegularCutomers(null,macObj.get("mac")+"",time1);
                            }
                        }
                    }
                }
            }
        } catch (NumberFormatException e) {
            return ResultVO.getFailed("Request Exception!",e);
        }
        return ResultVO.getSuccess("Success!");
    }

    @ApiOperation(value="保存访客信息（颜值，性别，停留时间）", notes="根据json对象保存")
    @ApiImplicitParam(name = "jsonObje", value = "JSON对象", required = true, dataType = "JsonObj", paramType = "path")
    @RequestMapping(value = "/postVisitorMember", method = RequestMethod.POST)
    public Object postVisitorMember(@RequestBody String arry) {
        try {
            JSONObject jsonObject = JSONObject.fromObject(arry);
            if (jsonObject == null || !jsonObject.containsKey("list")) {
                return ResultVO.getFailed("Request parameters no allow null");
            }
            JSONArray list = JSONArray.fromObject(jsonObject.get("list"));
            String mac = jsonObject.get("mac") + "";
            Assets assets = assetsService.findByMac(jsonObject.get("mac")+"");
            if (assets==null){
                assets = assetsService.saveAssets(null,  jsonObject.get("mac") + "",jsonObject.get("mac") + "",(short)1);
            }
            for (int i = 0; i < list.size(); i++) {
                JSONObject mObj = JSONObject.fromObject(list.get(i));
                visitMemberService.saveVisitMember(null, Integer.parseInt(mObj.get("age") + ""), Short.parseShort(mObj.get("gender")+""),
                        Integer.parseInt(mObj.get("beauty") + ""), Integer.parseInt(mObj.get("stay") + ""),
                        Timestamp.valueOf(mObj.get("stamp") + ""), assets.getId(), new Timestamp(System.currentTimeMillis()));
            }
        } catch (NumberFormatException e) {
            return ResultVO.getFailed("Request Exception",e);
        }
        return ResultVO.getSuccess("Success");
    }
}
