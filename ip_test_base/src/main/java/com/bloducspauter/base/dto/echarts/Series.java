package com.bloducspauter.base.dto.echarts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

/**
 * @author Bloduc Spauter
 *
 */
@ApiModel(description = "显示的具体曲线图内容")
public class Series {
    @ApiModelProperty("数据名称")
    private String name;
    @ApiModelProperty("连线类型（比如直线虚线），固定为直线")
    public static final String TYPE ="line";
    @Setter
    @ApiModelProperty("设置是否为光滑的曲线,默认为true")
    private boolean smooth=true;
    @ApiModelProperty("拐点类型,默认为圆圈")
    @Setter
    private String symbol="circle";
    @ApiModelProperty("拐点的大小，默认3")
    @Setter
    private int symbolSize=3;
    @Setter
    @ApiModelProperty("具体的数据")
    private Object[] data;
}
