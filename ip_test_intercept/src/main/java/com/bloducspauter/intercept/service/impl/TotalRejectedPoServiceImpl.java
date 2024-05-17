package com.bloducspauter.intercept.service.impl;

import com.bloducspauter.base.po.TotalRejectedPo;
import com.bloducspauter.intercept.mapper.TotalRejectedPoMapper;
import com.bloducspauter.intercept.service.TotalRejectedPoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

/**
 * @author Bloduc Spauter
 */
@Service
public class TotalRejectedPoServiceImpl implements TotalRejectedPoService {
    @Resource
    TotalRejectedPoMapper totalRejectedPoMapper;

    @Override
    public TotalRejectedPo totalRejectedPo() {
        TotalRejectedPo totalRejectedPo = totalRejectedPoMapper.getRejectedIps();
        // 获取当前时间
        LocalDateTime dateTime = LocalDateTime.now();
        Date date = Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        totalRejectedPo.setCreateTime(date);
        return totalRejectedPo;
    }
}
