package com.bloducspauter.intercept.controller;

import com.bloducspauter.base.dto.echarts.Legend;
import com.bloducspauter.base.dto.echarts.Option;
import com.bloducspauter.base.dto.echarts.XAxis;
import com.bloducspauter.base.dto.echarts.YAxis;
import com.bloducspauter.base.po.CountResultPo;
import com.bloducspauter.intercept.service.FacilityInformationCurrentRequestService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.awt.*;

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
        return service.countCurrent(s,0);
    }

    @ApiOperation("获取最近30s内被暂时限制的ip和设备数量")
    @GetMapping("/total_reject")
    public CountResultPo showCurrentRejected() {
        return service.currentRejectIps();
    }

    @ApiOperation("获取折线图数据")
    @GetMapping("/current_count")
    public Option getOption() {
        //此处写死了
        int start=60;
        int end=0;
        int interval=5;
        Option option=new Option();
        Legend legend=new Legend();
        option.setLegend(legend);
        XAxis xAxis=new XAxis();
        YAxis yAxis=new YAxis();
        return option;

    }
}
