package com.bloducspauter.intercept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bloducspauter.base.dto.ResultDto;
import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.entities.FacilityInformationCurrentRequest;
import com.bloducspauter.base.po.CountResultPo;
import com.bloducspauter.intercept.mapper.FacilityInformationCurrentRequestMapper;
import com.bloducspauter.intercept.service.FacilityInformationCurrentRequestService;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Map;


/**
 * @author Bloduc Spauter
 */
@Service
public class FacilityInformationCurrentRequestServiceImpl
        implements FacilityInformationCurrentRequestService {
    @Resource
    FacilityInformationCurrentRequestMapper facilityInformationCurrentRequestMapper;

    @Override
    public int addFacilityInformation(FacilityInformation fi) {
        FacilityInformationCurrentRequest facilityInformationCurrentRequest = new FacilityInformationCurrentRequest();
        facilityInformationCurrentRequest.setId(fi.getId());
        facilityInformationCurrentRequest.setTotal(fi.getTotalRequest());
        facilityInformationCurrentRequest.setReject(fi.getRejectedRequest());
        facilityInformationCurrentRequest.setIp(fi.getIpAddress());
        LocalDateTime dateTime = LocalDateTime.now();
        Date date =Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        facilityInformationCurrentRequest.setUpdateTime(date);
        return facilityInformationCurrentRequestMapper.insert(facilityInformationCurrentRequest);
    }

    @Override
    public CountResultPo countCurrent(int start,int end) {
        QueryWrapper<FacilityInformationCurrentRequest>queryWrapper=new QueryWrapper<>();
        queryWrapper.select("SUM(total) as total",
                "SUM(reject) as rejected",
                "COUNT(DISTINCT ip) as ips",
                "COUNT(DISTINCT id) as ids"
                );
        LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(start);
        LocalDateTime thirtySecondNear = LocalDateTime.now().minusSeconds(end);
        queryWrapper.lt("update_time", thirtySecondsAgo);
        queryWrapper.ge("update_time",thirtySecondNear);
        Map<String, Object> stats = facilityInformationCurrentRequestMapper.selectMaps(queryWrapper).get(0);
        String total=stats.get("total").toString();
        String rejected=stats.get("rejected").toString();
        String ids=stats.get("ids").toString();
        String ips=stats.get("ips").toString();
        CountResultPo countResultPo=new CountResultPo();
        countResultPo.setTotalId(Integer.parseInt(ids));
        countResultPo.setTotalRequests(Integer.parseInt(total));
        countResultPo.setRejectedRequest(Integer.parseInt(rejected));
        countResultPo.setIpAddress(Integer.parseInt(ips));
        LocalDateTime dateTime = LocalDateTime.now();
        Date date =Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        countResultPo.setCreateTime(date);
        return countResultPo;
    }

    @Override
    public CountResultPo currentRejectIps() {
        QueryWrapper<FacilityInformationCurrentRequest>queryWrapper=new QueryWrapper<>();
        queryWrapper.select("SUM(total) as total",
                "COUNT(DISTINCT ip) as ips",
                "COUNT(DISTINCT id) as ids"
        );
        queryWrapper.lt("reject",0);
        LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(30);
        queryWrapper.lt("update_time", thirtySecondsAgo);
        Map<String, Object> stats = facilityInformationCurrentRequestMapper.selectMaps(queryWrapper).get(0);
        String ids=stats.get("id").toString();
        String ips=stats.get("ip").toString();
        CountResultPo countResultPo=new CountResultPo();
        countResultPo.setTotalId(Integer.parseInt(ids));
        countResultPo.setIpAddress(Integer.parseInt(ips));
        LocalDateTime dateTime = LocalDateTime.now();
        Date date =Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        countResultPo.setCreateTime(date);
        return countResultPo;
    }


}
