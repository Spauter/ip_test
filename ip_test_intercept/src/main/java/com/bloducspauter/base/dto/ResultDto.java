package com.bloducspauter.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author Bloduc Spauter
 *
 */
@AllArgsConstructor
@Data
@ApiModel(value = "通用请求结果")
public class ResultDto {
    @ApiModelProperty(value = "状态码")
    private Integer code;
    @ApiModelProperty(value = "消息,可以为null")
    private String msg;
    @ApiModelProperty(value = "数据，可以为null")
    private Object data;
}
