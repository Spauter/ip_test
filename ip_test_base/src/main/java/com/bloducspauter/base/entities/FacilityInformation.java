package com.bloducspauter.base.entities;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * 请求访问设备基本信息
 *
 * @author Bloduc Spauter
 */
@Data
@TableName(value = "request_entity")
@AllArgsConstructor
public class FacilityInformation {
    /**
     * 设备ID
     */
    @TableId
    private Integer id;
    /**
     * 操作系统名字
     */
    private String operatingSystem;
    /**
     * 浏览器名字
     */
    private String browser;
    /**
     * 总请求访问次数
     */
    private Integer totalRequest;
    /**
     * 被拒接访问次数
     */
    private Integer rejectedRequest;
    /**
     * 请求Ip地址
     */
    private String ipAddress;
}
