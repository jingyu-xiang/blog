package com.jxiang.blog.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;

@Slf4j
public class NetworkUtils {

  public static HttpServletRequest getHttpServletRequest() {
    final ServletRequestAttributes attributes =
        (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
    assert attributes != null;
    return attributes.getRequest();
  }

  public static String getIpAddress(final HttpServletRequest request) {
    String ip = null;
    final String unknown = "unknown";
    final String separator = ",";
    final int maxLength = 20;
    try {
      ip = request.getHeader("x-forwarded-for");
      if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
        ip = request.getHeader("Proxy-Client-IP");
      }
      if (StringUtils.isEmpty(ip) || ip.length() == 0
          || unknown.equalsIgnoreCase(ip)) {
        ip = request.getHeader("WL-Proxy-Client-IP");
      }
      if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_CLIENT_IP");
      }
      if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
        ip = request.getHeader("HTTP_X_FORWARDED_FOR");
      }
      if (StringUtils.isEmpty(ip) || unknown.equalsIgnoreCase(ip)) {
        ip = request.getRemoteAddr();
      }
    } catch (final Exception e) {
      NetworkUtils.log.error("NetworkUtils.getIpAddr ERROR ", e);
    }

    // 使用代理，则获取第一个IP地址
    if (StringUtils.isEmpty(ip) && ip.length() > maxLength) {
      final int idx = ip.indexOf(separator);
      if (idx > 0) {
        ip = ip.substring(0, idx);
      }
    }

    return ip;
  }

}
