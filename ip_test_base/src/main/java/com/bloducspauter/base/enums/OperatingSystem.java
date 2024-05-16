package com.bloducspauter.base.enums;

import lombok.Getter;
/**
 * 操作系统枚举类
 * @author Bloduc Spauter
 *
 */
@Getter
public enum OperatingSystem {
    /**
     * Windows 操作系统
     */
    WINDOWS("Windows"),

    /**
     * MAC OS 操作系统
     */
    MAC_OS("MAC"),

    CENTOS("Centos"),

    UBUNTU("Ubuntu"),

    DEBIAN("Debian"),

    ANDROID("Android"),

    UNKNOWN(""),

    IOS("iOS");


    private final String name;

    OperatingSystem(String name) {
        this.name = name;
    }

}
