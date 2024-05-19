package com.bloducspauter.base.dto.echarts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

/**
 * @author Bloduc Spauter
 *
 */
@Setter
@ApiModel(description = "图列相关信息")
public  class Legend {
    @ApiModelProperty("图列数据，默认为\"请求数\"和\"拦截数\"")
    private String[] data ={"请求数","拦截数"};
}
