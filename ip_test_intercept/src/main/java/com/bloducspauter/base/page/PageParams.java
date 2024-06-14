package com.bloducspauter.base.page;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * 存储分页的参数
 *
 * @author Bloduc Spauter
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class PageParams {
    private Long pageOn = 1L;
    private Long pageSize = 5L;
}