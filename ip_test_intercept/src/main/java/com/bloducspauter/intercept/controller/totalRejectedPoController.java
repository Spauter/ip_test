package com.bloducspauter.intercept.controller;

import com.bloducspauter.base.po.TotalRejectedPo;
import com.bloducspauter.intercept.service.TotalRejectedPoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;

/**
 * @author Bloduc Spauter
 *
 */
@RequestMapping("/intercept/")
@Api("Ip统计")
public class totalRejectedPoController {
    @Resource
    private TotalRejectedPoService service;

    @ApiOperation("统计当前时间被封禁的ip数量")
    public TotalRejectedPo getTotalRejectedPo() {
        return service.totalRejectedPo();
    }
}
