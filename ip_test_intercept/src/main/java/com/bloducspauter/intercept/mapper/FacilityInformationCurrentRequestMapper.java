package com.bloducspauter.intercept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloducspauter.base.entities.FacilityInformationCurrentRequest;
import org.apache.ibatis.annotations.Update;

/**
 * @author Bloduc Spauter
 *
 */
public interface FacilityInformationCurrentRequestMapper
        extends BaseMapper<FacilityInformationCurrentRequest> {

    /**
     * 清空当前表中所有数据
     * @return 0 or 1
     */
    @Update("TRUNCATE TABLE current_request_entity")
    int  truncate();
}
