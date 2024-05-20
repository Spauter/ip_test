package com.bloducspauter.base.dto.echarts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author Bloduc Spauter
 *
 */
@Setter
@Getter
@ApiModel("图列相关信息")
public  class Legend   {
    @ApiModelProperty("图列数据，默认为\"请求数\"和\"拦截数\"")
    public String[] data ={"请求数","拦截数"};
}
