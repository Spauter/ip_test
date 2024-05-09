package com.bloducspauter.base.sort;

import com.bloducspauter.base.enums.SortWay;
import lombok.Data;
import lombok.Setter;

/**
 * 存储排序的参数
 *
 * @author Bloduc Spauter
 */
@Data
public class SortParams {
    /**
     * 需要排序的字段
     */
    private String sortBy;
    /**
     * 顺序还是倒序,使用{@link SortWay}
     */

    private boolean sort;
    /**
     * 选择排序的前面数量
     */

    private String limit;
}
