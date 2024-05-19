package com.bloducspauter.base.dto.echarts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

/**
 * @author Bloduc Spauter
 *
 */
@Setter
@ApiModel("X轴相关简单设置")
public class XAxis {
    public static final String TYPE ="category";
    public static final String NAME="时间";
    @ApiModelProperty("X轴数据")
    private Object[] data;
    @ApiModelProperty("是否显示格子")
    private boolean showLine;
}
