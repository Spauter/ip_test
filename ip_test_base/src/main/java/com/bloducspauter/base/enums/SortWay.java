package com.bloducspauter.base.enums;
/**
 * 表示顺序还是倒序
 * @author Bloduc Spauter
 *
 */
public enum SortWay {
    /**
     * 降序
     */
    ASC("ASC"),

    /**
     * 升序
     */
    DESC("DESC");


    private final String sort;

    SortWay(String sort) {
        this.sort = sort;
    }
}
