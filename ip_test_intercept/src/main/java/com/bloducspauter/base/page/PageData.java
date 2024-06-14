package com.bloducspauter.base.page;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 分页查询的数据
 *
 * @param <T>
 * @author Bloduc Spauter
 */
@Data
@AllArgsConstructor
@ApiModel("分页查询的数据")
public class PageData<T> {

    @ApiModelProperty("分页查询的数据总量")
    private Long total;

    @ApiModelProperty("当前分页查询的数量")
    private Long pageSize;

    /**
     * 分页查询结果
     */
    @ApiModelProperty("分页查询结果")
    private List<T> entities;
}
