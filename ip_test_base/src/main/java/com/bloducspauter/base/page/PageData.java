package com.bloducspauter.base.page;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

/**
 * 分页查询的数据
 * @param <T>
 * @author Bloduc Spauter
 */
@Data
@AllArgsConstructor
public class PageData<T>{

    private Long total;

    private Long pageSize;
    /**
     * 分页查询结果
     */
    private List<T> entities;
}
