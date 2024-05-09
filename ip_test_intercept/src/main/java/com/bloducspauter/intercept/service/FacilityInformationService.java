package com.bloducspauter.intercept.service;

import com.bloducspauter.base.page.PageData;
import com.bloducspauter.base.page.PageParams;
import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.sort.SortData;
import com.bloducspauter.base.sort.SortParams;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public interface FacilityInformationService {
    /**
     * 增加访问信息,如果信息已经存在就更新信息
     * @param requestFacilityInformation 实体
     */
    int insert(FacilityInformation requestFacilityInformation);

    /**
     * 更新访问信息
     * @param requestFacilityInformation 实体
     */
    int update(FacilityInformation requestFacilityInformation);

    /**
     * 删除访问信息
     * @param requestFacilityInformation 实体
     */
    int delete(FacilityInformation requestFacilityInformation);

    /**
     * 根据设备Id查找实体信息
     * @param id ID
     */
    FacilityInformation findById(int id);

    /**
     * 查找所有实体信息
     * @return {@code List}
     */
    List<FacilityInformation> findAll();

    /**
     * 分页查询访问信息
     * @param pageParams 请求的分页参数
     */
    PageData<FacilityInformation> selectPage(PageParams pageParams);

    SortData<FacilityInformation> sortData(SortParams sortParams);
}
