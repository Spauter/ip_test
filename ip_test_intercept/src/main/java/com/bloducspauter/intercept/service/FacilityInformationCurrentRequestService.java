package com.bloducspauter.intercept.service;


import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.po.CountResultPo;


/**
 * @author Bloduc Spauter
 *
 */
public interface FacilityInformationCurrentRequestService {
    /**
     * 添加实时统计信息
     * @param fi 当前{@code FacilityInformation}数据
     * @return
     */
    int addFacilityInformation(FacilityInformation fi);

    /*
     *统计最近20s内被拦截的访问信息
     */
    CountResultPo currentRejectIps();

    /**
     * 统计最近几秒到几秒的数据
     * @param start 离当前时间较近的时间点
     * @param end 离当前时间较远的时间点
     */
    CountResultPo countCurrent(int start,int end);
}
