package com.bloducspauter.base.dto.echarts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Bloduc Spauter
 *
 */
@ApiModel(description = "y轴相关简单设计",value = "yAxis")
public class YAxis {
    public static final String TYPE ="value";
    @ApiModelProperty("X轴数据")
    private Object[] data;
    @ApiModelProperty("是否显示网格")
    private boolean showLine;
}
