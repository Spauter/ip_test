package com.bloducspauter.base.enums;

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
