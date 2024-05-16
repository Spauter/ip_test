package com.bloducspauter.intercept.service;

import com.bloducspauter.base.page.PageData;
import com.bloducspauter.base.page.PageParams;
import com.bloducspauter.base.entities.FacilityInformation;
import com.bloducspauter.base.sort.SortData;
import com.bloducspauter.base.sort.SortParams;
import io.swagger.models.auth.In;
import org.springframework.stereotype.Service;

import java.util.List;
/**
 * @author Bloduc Spauter
 *
 */
@Service
public interface FacilityInformationService {
    /**
     * 增加访问信息,如果信息已经存在就更新信息
     * @param requestFacilityInformation 实体
     */
    int insert(FacilityInformation requestFacilityInformation);

    /**
     * 更新访问信息频率
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

    /**
     * 对信息进行排序
     * @param sortParams 排序参数
     * @return
     */
    SortData<FacilityInformation> sortData(SortParams sortParams);

    int isBanAnIp(Integer id, Integer status);

}
