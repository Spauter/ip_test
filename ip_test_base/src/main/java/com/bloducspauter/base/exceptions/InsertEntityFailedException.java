package com.bloducspauter.base.exceptions;

import java.sql.SQLException;

/**
 * 当数据库插入字段失败时抛出此异常
 * @author Bloduc Spauter
 */
public class InsertEntityFailedException extends SQLException{
    private static final long serialVersionUID = 185483881L;

    /**
     * {@code InsertEntityFailedException}构造没有指定详细信息消息的对象。
     */
    public InsertEntityFailedException() {
        super("Insert entity failed");
    }

    /**
     * {@code InsertEntityFailedException}构造具有指定详细信息消息的对象。
     * @param message 详细信息消息。
     */
    public InsertEntityFailedException(String message) {
        super(message);
    }

    /**
     * {@code InsertEntityFailedException}构造具有实体类的对象。
     * @param entity 被操作的实体类
     */
    public InsertEntityFailedException(Class<?> entity) {
        super("Insert Entity Failed: " + entity.getSimpleName());
    }
}
