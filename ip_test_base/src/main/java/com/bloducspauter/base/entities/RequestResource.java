package com.bloducspauter.base.entities;

import com.baomidou.mybatisplus.annotation.TableName;

/**
 * 被请求访问的资源
 * @author Bloduc Spauter
 */
@TableName
public class RequestResource {
    private Integer id;
    private String resourceName;

}
