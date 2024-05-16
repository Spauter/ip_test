package com.bloducspauter.base.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;

/**
 * @author Bloduc Spauter
 *
 */
@AllArgsConstructor
@ApiModel("请求结果")
public class ResultDto {
    @ApiModelProperty("状态码")
    private Integer code;
    @ApiModelProperty("消息,可以为null")
    private String msg;
    @ApiModelProperty("数据，可以为null")
    private Object data;
}
