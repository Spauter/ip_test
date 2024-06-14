package com.bloducspauter.base.po;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author Bloduc Spauter
 *
 */
@ApiModel("当前访问数量统计")
@Data
public class CurrentCountForRequestIps {
    @ApiModelProperty("当前被拒绝的IP数量")
    private int totalRejected;
    @ApiModelProperty("当前访问Ip数量")
    private int totalRequest;
    @ApiModelProperty("获取到此数据的时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
