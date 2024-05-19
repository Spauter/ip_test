package com.bloducspauter.base.dto.echarts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

/**
 * @author Bloduc Spauter
 *
 */
@Setter
@ApiModel(description = "X轴相关简单设置",value = "xAxis")
public class XAxis {
    public static final String TYPE ="category";
    @ApiModelProperty("X轴数据单位,默认为时间")
    private  String name="时间";
    @ApiModelProperty("X轴数据")
    private Object[] data;
    @ApiModelProperty("是否显示网格")
    private boolean showLine;
}
