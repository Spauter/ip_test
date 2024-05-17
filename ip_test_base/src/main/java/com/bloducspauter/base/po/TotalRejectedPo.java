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
@ApiModel("被拒绝的访问数量")
@Data
public class TotalRejectedPo {
    @ApiModelProperty("被拒绝的IP数量")
    private int totalRejected;
    @ApiModelProperty("获取到此数据的时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
}
