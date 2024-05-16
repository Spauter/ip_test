package com.bloducspauter.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 对于{@code QueryFacilityInformationParamsDto}的查询条件，或者用于返回简单查询结果
 * @author Bloduc Spauter
 *
 */
@Data
@ApiModel("对发出请求对象的查询条件或者状态，或者用于返回简单查询结果")
public class QueryFacilityInformationParamsDto {
    @ApiModelProperty("设备id，用于精确查询")
    private Integer id;

    @ApiModelProperty("操作系统名称")
    private String operatingSystem;

    @ApiModelProperty("对象的状态,注意-1是操作失败")
    private Integer status;
}
