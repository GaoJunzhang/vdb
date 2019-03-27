package com.seeyoo.zm.visit.controller;

import com.seeyoo.zm.visit.model.Assets;
import com.seeyoo.zm.visit.result.JsonResult;
import com.seeyoo.zm.visit.result.ResultCode;
import com.seeyoo.zm.visit.service.AssetsService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/assets")
@Api(value = "店面资产",tags = {"每个终端mac为一个店面，店面资产"})
public class AssetsController {
    @Autowired
    private AssetsService assetsService;

    @RequestMapping(value = "/getAssets",method = RequestMethod.GET)
    @ApiOperation(value = "getAssets")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "mac",value = "mac",dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "name",value = "name",dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "page",value = "page",dataType = "Integer"),
            @ApiImplicitParam(paramType = "query",name = "rows",value = "rows",dataType = "Integer"),
            @ApiImplicitParam(paramType = "query",name = "sortType",value = "sortType",dataType = "String"),
            @ApiImplicitParam(paramType = "query",name = "sortValue",value = "sortValue",dataType = "String"),
    })
    public Map<String,Object> getAssets(String mac,String name, @RequestParam(name = "page",defaultValue = "1") int page,@RequestParam(name = "rows",defaultValue = "10") int rows,
                                        @RequestParam(name = "sortType",defaultValue = "desc") String sortType, @RequestParam(name = "sortValue",defaultValue = "time") String sortValue){
        Map<String,Object> map = new HashMap<String, Object>();
        Page<Assets> page1 = assetsService.collages(mac,name,page-1,rows,sortType,sortValue);
        map.put("rows",page1.getContent());
        map.put("total",page1.getTotalPages());
        return map;
    }

    @RequestMapping(value = "/postAssets",method = RequestMethod.POST)
    @ApiOperation(value = "postAssets")
    @ApiImplicitParams({
            @ApiImplicitParam(paramType = "query",name = "id",value = "id",dataType = "Integer"),
            @ApiImplicitParam(paramType = "query",name = "name",value = "name",dataType = "String"),
    })
    public Object postAssets(@RequestParam(name = "id",required = true) Integer id, @RequestParam(name = "name",required = true) String name,@RequestParam("type") short type){
        JsonResult jsonResult;
        Assets assets = new Assets();
        assets.setId(id);
        assets.setName(name);
        try {
            assetsService.saveAssets(id,name,null,type);
        } catch (Exception e) {
            e.printStackTrace();
            jsonResult = new JsonResult(ResultCode.EXCEPTION, "请求异常", e);
            return jsonResult;
        }
        jsonResult = new JsonResult(ResultCode.SUCCESS, "成功", null);
        return jsonResult;
    }
}
