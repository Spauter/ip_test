package com.bloducspauter.intercept.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.entities.FacilityInformationCurrentRequest;
import com.bloducspauter.base.po.CountResultPo;
import com.bloducspauter.intercept.mapper.FacilityInformationCurrentRequestMapper;
import com.bloducspauter.intercept.service.FacilityInformationCurrentRequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger log = LoggerFactory.getLogger(FacilityInformationCurrentRequestServiceImpl.class);
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
        queryWrapper.gt("update_time", thirtySecondsAgo);
        queryWrapper.le("update_time",thirtySecondNear);
        Map<String, Object> stats = facilityInformationCurrentRequestMapper.selectMaps(queryWrapper).get(0);
        return getCountResultPo(stats);
    }

    @Override
    public CountResultPo currentRejectIps() {
        QueryWrapper<FacilityInformationCurrentRequest>queryWrapper=new QueryWrapper<>();
        queryWrapper.select("SUM(total) as total",
                "SUM(reject) as rejected",
                "COUNT(DISTINCT ip) as ips",
                "COUNT(DISTINCT id) as ids"
        );
        queryWrapper.gt("reject",0);
        LocalDateTime thirtySecondsAgo = LocalDateTime.now().minusSeconds(60);
        queryWrapper.gt("update_time", thirtySecondsAgo);
        Map<String, Object> stats = facilityInformationCurrentRequestMapper.selectMaps(queryWrapper).get(0);
        return getCountResultPo(stats);
    }

    private CountResultPo getCountResultPo( Map<String, Object> stats) {
        stats.putIfAbsent("total",0);
        stats.putIfAbsent("ids", 0);
        stats.putIfAbsent("ips",0);
        stats.putIfAbsent("rejected",0);
        String ids=stats.get("ids").toString();
        String ips=stats.get("ips").toString();
        String total=stats.get("total").toString();
        String reject=stats.get("rejected").toString();
        CountResultPo countResultPo=new CountResultPo();
        countResultPo.setTotalId(Integer.parseInt(ids));
        countResultPo.setIpAddress(Integer.parseInt(ips));
        countResultPo.setTotalRequests(Integer.parseInt(total));
        countResultPo.setRejectedRequest(Integer.parseInt(reject));
        LocalDateTime dateTime = LocalDateTime.now();
        Date date =Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
        countResultPo.setCreateTime(date);
        return countResultPo;
    }

}
