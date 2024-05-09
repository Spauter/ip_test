package com.bloducspauter.base.exceptions;

import com.bloducspauter.base.enums.CommonError;

/**
 * 参数不合法时抛出此异常,继承自{@code IllegalArgumentException}
 *
 * @author Bloduc Spauter
 */
public class IllegalParamException extends IllegalArgumentException {
    //圆神UID
    private static final long serialVersionUID = 192099489L;

    /**
     * {@code IllegalArgumentException}构造具有指定详细信息消息的对象。
     *
     * @param message 详细信息消息。
     */
    public IllegalParamException(String message) {
        super(message);
    }

    /**
     * {@code IllegalArgumentException}构造具有指定通用错误信息{@code CommonError}的对象
     *
     * @param error 通用错误信息{@code CommonError}
     */
    public static void cast(CommonError error) {
        String message = error.getErrMessage();
        throw new IllegalParamException(message);
    }

    /**
     * {@code IllegalArgumentException}构造具有指定通用错误信息{@code CommonError}和详细参数消息{@code param}的对象
     *
     * @param error 通用错误信息{@code CommonError}
     * @param param 详细参数消息
     */
    public static void cast(CommonError error, Object param) {
        String message = error.getErrMessage();
        throw new IllegalParamException(message + ":" + param.toString());
    }
}
