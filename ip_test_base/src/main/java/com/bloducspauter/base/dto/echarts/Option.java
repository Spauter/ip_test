package com.bloducspauter.base.dto.echarts;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Setter;

import java.util.List;

/**
 * 平滑曲线图参数
 *
 * @author Bloduc Spauter
 */
@Setter
@ApiModel(description = "平滑曲线图的相关参数")
public class Option {
    @ApiModelProperty("图列数据")
    private Legend legend;
    @ApiModelProperty("颜色配置")
    private String[] color = {"#289df5", "#fbc01b"};
    @ApiModelProperty("X轴相关数据")
    private XAxis xAxis;
    @ApiModelProperty("Y轴相关数据")
    private YAxis yAxis;
    @ApiModelProperty("具体统计图数据")
    List<Series> series;
}
