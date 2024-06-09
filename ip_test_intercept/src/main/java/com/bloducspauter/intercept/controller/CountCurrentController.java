package com.bloducspauter.intercept.controller;

import com.bloducspauter.base.dto.ResultDto;
import com.bloducspauter.base.dto.echarts.*;
import com.bloducspauter.base.po.CountResultPo;
import com.bloducspauter.intercept.service.FacilityInformationCurrentRequestService;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Bloduc Spauter
 */
@RestController
@Api("ip设备实时统计控制层")
@RequestMapping("/intercept")
@Slf4j
public class CountCurrentController {
    @Resource
    private FacilityInformationCurrentRequestService service;

    @Resource
    RedisTemplate<String, Object> redisTemplate;

    @GetMapping("/total_count")
    @ApiOperation("查询最近时间访问情况,其中参数s为秒数")
    public CountResultPo showCurrentCount(int s) {
        return service.countCurrent(s, 0);
    }

    @ApiOperation("获取最近30s内被暂时限制的ip和设备数量")
    @GetMapping("/total_reject")
    public CountResultPo showCurrentRejected(int before) {
        return service.currentRejectIps(before);
    }

    @ApiOperation("获取折线图数据，用于Echats")
    @GetMapping("/current_count_e")
    public List<Series> getCurrentCountInE() throws JsonProcessingException {
        Legend legend = new Legend();
        int start = 60;
        Series total = new Series();
        total.setName(legend.data[0]);
        Series reject = new Series();
        reject.setName(legend.data[1]);
        do {
            CountResultPo countResultPo = service.countCurrent(start, start - 5);
            total.getData().add(countResultPo.getTotalRequests());
            reject.getData().add(countResultPo.getRejectedRequest());
            start -= 5;
        } while (start > 0);
        List<Series> series = new ArrayList<>();
        series.add(total);
        series.add(reject);
        return series;
    }

    @ApiOperation("获取折线图数据，用于amcharts")
    @GetMapping("current_count_a")
    public Map<Integer, Object> getCurrentCountIna() {
        Map<Integer, Object> map = new ConcurrentHashMap<>();
        int start = 60;
        do {
            CountResultPo countResultPo = service.countCurrent(start, start - 5);
            map.put(start,countResultPo);
            start -= 5;
        } while (start > 0);
        return map;
    }

    @ApiOperation("设置最大拦截数量,没有默认为1000")
    @GetMapping("max_allow")
    public ResultDto setMaxAllow(int maximum) {
        if (maximum < 200) {
            return new ResultDto(HttpStatus.NOT_ACCEPTABLE.value(), "最小为200", maximum);
        }
        redisTemplate.opsForValue().set("max_allow", maximum);
        return new ResultDto(HttpStatus.OK.value(), "操作成功", maximum);
    }
}

