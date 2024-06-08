package com.bloducspauter.base.dto.echarts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Bloduc Spauter
 *
 */
@Data
@ApiModel("显示的具体曲线图内容")
public class Series   {
    @ApiModelProperty("数据名称")
    private String name;
    @ApiModelProperty("连线类型（比如直线虚线），默认为直线")
    private  String type ="line";
    @ApiModelProperty("设置是否为光滑的曲线,默认为true")
    private boolean smooth=true;
    @ApiModelProperty("拐点类型,默认为圆圈")
    private String symbol="circle";
    @ApiModelProperty("拐点的大小，默认3")
    private int symbolSize=3;
    @ApiModelProperty("具体的数据")
    public List<Object> data=new ArrayList<>();
}
