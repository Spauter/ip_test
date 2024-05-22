package com.bloducspauter.base.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 发出请求的对象的基本信息
 * @author Bloduc Spauter
 */
@Data
@TableName(value = "request_entity")
@AllArgsConstructor
@ApiModel(value="请求的设备基本信息")
@NoArgsConstructor
public class FacilityInformation implements Serializable {
    /**
     * 设备ID
     */
    @ApiModelProperty("设备ID")
    private Integer id;
    /**
     * 操作系统名字
     */
    @ApiModelProperty("操作系统名称")
    private String operatingSystem;
    /**
     * 浏览器名字
     */
    @ApiModelProperty("浏览器名称")
    private String browser;
    /**
     * 总请求访问次数
     */
    @ApiModelProperty("总请求访问次数")
    private Integer totalRequest;
    /**
     * 被拒接访问次数
     */
    @ApiModelProperty("被拒接访问次数")
    private Integer rejectedRequest;
    /**
     * 请求Ip地址
     */
    @ApiModelProperty("发出请求的IP地址")
    @TableId
    private String ipAddress;
    /**
     * 状态0.正常，1封禁
     */
    @ApiModelProperty("状态。0.正常，1封禁")
    private Integer status;
}
