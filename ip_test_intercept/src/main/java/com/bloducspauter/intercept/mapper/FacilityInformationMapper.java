package com.bloducspauter.intercept.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.bloducspauter.base.entities.FacilityInformation;
import org.apache.ibatis.annotations.Update;

/**
 * @author Bloduc Spauter
 *
 */
public interface FacilityInformationMapper extends BaseMapper<FacilityInformation> {

    @Update("TRUNCATE TABLE request_entity")
    int  truncate();
}
