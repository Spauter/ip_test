package com.bloducspauter.base.sort;

import com.bloducspauter.base.enums.SortWay;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 存储排序的参数
 *
 * @author Bloduc Spauter
 */
@Data
@ApiModel("存储排序的参数")
public class SortParams {
    /**
     * 需要排序的字段
     */
    @ApiModelProperty("需要排序的字段")
    private String sortBy;
    /**
     * 顺序还是倒序,使用{@link SortWay}
     */
    @ApiModelProperty("顺序还是倒序，ASC或者DESC")
    private String sort;
    /**
     * 选择排序的数量限制
     */
    @ApiModelProperty("选择排序的数量限制")
    private String limit;
}
