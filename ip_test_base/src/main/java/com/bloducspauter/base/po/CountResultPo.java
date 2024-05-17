package com.bloducspauter.base.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * 请求对象统计结果
 *
 * @author Bloduc Spauter
 */
@Data
@ApiModel("请求对象统计结果")
public class CountResultPo {
    @ApiModelProperty("总请求数量")
    private Integer totalRequests;
    @ApiModelProperty("总拒绝数量")
    private Integer rejectedRequest;
    @ApiModelProperty("总请求Ip数量")
    private Integer ipAddress;
    @ApiModelProperty("总设备数量")
    private Integer totalId;
    @ApiModelProperty("获取到此数据的时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
