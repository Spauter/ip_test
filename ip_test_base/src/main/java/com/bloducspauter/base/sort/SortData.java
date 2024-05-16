package com.bloducspauter.base.sort;

import com.bloducspauter.base.enums.SortWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel("排序结果")
public class SortData<T> {
    /**
     * 需要排序的字段
     */
    @Setter
    @ApiModelProperty("需要排序的字段")
    private String sortBy;

    /**
     * 排序结果
     */
    @ApiModelProperty("排序结果")
    private List<T> entities;
}
