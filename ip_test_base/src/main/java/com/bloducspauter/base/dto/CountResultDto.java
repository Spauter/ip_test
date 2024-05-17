package com.bloducspauter.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 请求对象统计结果
 *
 * @author Bloduc Spauter
 */
@Data
@ApiModel("请求对象统计结果")
public class CountResultDto {
    @ApiModelProperty("总请求数量")
    private Integer totalRequests;
    @ApiModelProperty("总拒绝数量")
    private Integer rejectedRequest;
    @ApiModelProperty("总请求Ip数量")
    private Integer ipAddress;
    @ApiModelProperty("总设备数量")
    private Integer totalId;
}
