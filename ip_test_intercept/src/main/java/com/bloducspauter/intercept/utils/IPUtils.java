package com.bloducspauter.intercept.utils;
import javax.servlet.http.HttpServletRequest;

/**
 * IP工具类
 */
public class IPUtils {

    public static final String SEMICOLON = ";";

    // Nginx代理传递的实际客户端 IP-header
    public static final String[] HEADERS_TO_TRY = {
            "X-Forwarded-For",
            "X-REAL-IP",
            "Proxy-Client-IP",
            "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR",
            "HTTP_X_FORWARDED",
            "HTTP_X_CLUSTER_CLIENT_IP",
            "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR",
            "HTTP_FORWARDED",
            "HTTP_VIA",
            "REMOTE_ADDR",
            "REMOTE-HOST"
    };

    /**
     * 获取客户端的IP地址
     */
    public static String getClientIp(HttpServletRequest request) {
        String clientIp = _getClientIp(request);
        if (null != clientIp && !clientIp.trim().isEmpty()) {
            return clientIp;
        }
        return request.getRemoteAddr();
    }

    private static String _getClientIp(HttpServletRequest request) {
        for (String header : HEADERS_TO_TRY) {
            String ip = request.getHeader(header);
            if (ip != null && ip.length() != 0 && !"unknown".equalsIgnoreCase(ip)) {
                return ip;
            }
        }
        return request.getRemoteAddr();
    }
}