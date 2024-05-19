package com.bloducspauter.intercept.service;


import com.bloducspauter.base.dto.ResultDto;
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
     *统计最近20s内接收到的对象信息
     */

    CountResultPo currentRejectIps();

    CountResultPo countCurrent(int s);
}
