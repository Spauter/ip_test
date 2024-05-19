package com.bloducspauter.base.dto.echarts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

/**
 * @author Bloduc Spauter
 *
 */
@ApiModel("图列相关信息")
public  class Legend {
    @ApiModelProperty("图列数据，固定为\"请求数\"和\"拦截数\"")
    public static final String[] DATA ={"请求数","拦截数"};
}
