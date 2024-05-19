package com.bloducspauter.intercept.controller;

import com.bloducspauter.base.po.CountResultPo;
import com.bloducspauter.intercept.service.FacilityInformationCurrentRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.Tag;
import org.apache.ibatis.annotations.Param;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Bloduc Spauter
 *
 */
@RestController
@Api("ip设备实时统计控制层")
@RequestMapping("/intercept")
public class CountCurrentController {
    @Resource
    private FacilityInformationCurrentRequestService service;

    @GetMapping("/total_count")
    @ApiOperation("查询最近时间访问情况,其中参数s为秒数")
    public CountResultPo showCurrentCount(int s) {
        return service.countCurrent(s);
    }

    @ApiOperation("获取最近30s内被暂时限制的ip和设备数量")
    @GetMapping("/total_reject")
    public CountResultPo showCurrentRejected() {
        return service.currentRejectIps();
    }
}
