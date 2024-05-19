package com.bloducspauter.base.entities;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @author Bloduc Spauter
 */
@ApiModel("请求对象实时统计数据")
@Data
@TableName("current_request_entity")
public class FacilityInformationCurrentRequest implements Serializable {
    @ApiModelProperty("ID")
    @TableId(type = IdType.AUTO)
    private Integer fid;
    @ApiModelProperty("发出请求设备的id")
    private Integer id;
    @ApiModelProperty("请求ip")
    private String ip;
    @ApiModelProperty("当前总请求数")
    private Integer total;
    @ApiModelProperty("当前被拦截量")
    private Integer reject;
    @ApiModelProperty("更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
}
