package com.bloducspauter.intercept.config.handler;


import com.bloducspauter.base.enums.CommonError;
import com.bloducspauter.base.exceptions.IllegalParamException;
import com.bloducspauter.base.exceptions.InsertEntityFailedException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 自定义异常
 * <p>
 * {@code RestControllerAdvice}表示是控制增强
 * </p>
 * <p>
 * {@code Order} 表示触发集别，值越低表示这个类去捕获其他异常的优先级越高
 * </p>
 * {@code ExceptionHandler} 表示余姚处理的异常类型
 * <p>
 * {@code  @ResponseStatus}表示返回的Http状态码响应
 * </p>
 *
 * @author Bloduc Spauter
 */
@Slf4j
@RestControllerAdvice
public class CountRequestControllerExceptionHandler {

    /**
     * 对项目的自定义异常类型进行处理
     *
     * @param e 被捕获的异常{@code IllegalParamException}
     * @return
     */
    @Order(-2000)
    @ExceptionHandler({IllegalParamException.class, NullPointerException.class,IllegalArgumentException.class,IllegalStateException.class})
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> customException(Exception e) {
        Map<String, Object> map = new HashMap<>();
        // 记录异常
        log.error(e.getMessage(), e);
        // 解析出异常信息
        String errMessage = e.getMessage();
        map.put("code", 500);
        if (e instanceof NullPointerException) {
            map.put("msg", "服务器可能接收到了空数据");
            map.put("cause", CommonError.REQUEST_NULL.getErrMessage());
        } else {
            map.put("msg", "好像有不合法的参数");
            map.put("cause", e.getCause());
        }
        return map;
    }

    @ExceptionHandler(InsertEntityFailedException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> customException(InsertEntityFailedException e) {
        Map<String, Object> map = new HashMap<>();
        log.error(e.getMessage(), e);
        String errMessage = "这个数据好像插不进去";
        map.put("code", 500);
        map.put("msg", errMessage);
        map.put("cause", e.getCause());
        return map;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> customException(MethodArgumentNotValidException exception) {
        Map<String, Object> map = new HashMap<>();
        BindingResult bindingResult = exception.getBindingResult();
        // 存储错误信息
        List<String> errors = new ArrayList<>();
        bindingResult.getFieldErrors().forEach(item -> {
            errors.add(item.getDefaultMessage());
        });
        // 将list中的错误信息拼接起来
        String errMessage = String.join(", ", errors);
        // 记录异常
        log.error("系统异常{}, 原因{}", exception.getMessage(), errMessage);
        map.put("code", 500);
        map.put("msg", errMessage);
        map.put("cause", exception.getCause());
        return map;
    }

    @ExceptionHandler(SQLException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleSQLException(SQLException e) {
        Map<String, Object> map = new HashMap<>();
        // 记录异常
        log.error("系统异常{}", e.getMessage(), e);
        // 解析出异常信息
        map.put("code", 500);
        map.put("msg", "数据库抽风了, 请稍后再试");
        map.put("cause", e.getCause());
        return map;
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Map<String, Object> handleException(Exception e) {
        Map<String, Object> map = new HashMap<>();
        // 记录异常
        log.error("服务器出现异常{}", e.getMessage(), e);
        // 解析出异常信息
        map.put("code", 500);
        map.put("msg", "服务器抽风了, 请稍后再试");
        map.put("cause", e.getCause());
        return map;
    }
}

