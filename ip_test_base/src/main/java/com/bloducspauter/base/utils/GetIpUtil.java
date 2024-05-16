package com.bloducspauter.base.utils;

import eu.bitwalker.useragentutils.UserAgent;
import lombok.extern.slf4j.Slf4j;

import javax.servlet.http.HttpServletRequest;

/**
 * 获取IP
 * @author Bloduc Spauter
 *
 */
public class GetIpUtil {
    /**
     * 在请求中分割获取到ip地址
     * @param request {@code HttpServletRequest}
     * @return ip地址
     */
    public String getIpAddress(HttpServletRequest request) {
        String ipAddress = request.getHeader("X-Forwarded-For");
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
        }
        return ipAddress.split(",")[0];
    }

    public int getId(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        UserAgent userAgentObj = UserAgent.parseUserAgentString(userAgent);
        int id = userAgentObj.getId();
        return id;
    }
}
