package com.bloducspauter.base.sort;

import com.bloducspauter.base.enums.SortWay;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 排序结果
 * @author Bloduc Spauter
 * @param <T>
 */
@AllArgsConstructor
@Getter
public class SortData<T> {
    /**
     * 需要排序的字段
     */
    @Setter
    private String sortBy;

    /**
     * 排序结果
     */
    private List<T> entities;

}
