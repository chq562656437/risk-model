package com.jk.risk.util;


import com.google.common.base.Throwables;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.Enumeration;

public class ExceptionUtil {
    private final static char COLON    = ':';
    private final static char NEWLINES = '\n';
    private static String HOSTNAME;

    static {
        try {
            HOSTNAME = InetAddress.getLocalHost().getHostAddress();
        } catch (UnknownHostException e) {
            HOSTNAME = "UnknownHost";
        }
    }

    @SuppressWarnings({"unchecked", "rawtypes"})
    public static String print(HttpServletRequest request, Throwable e) {
        StringBuffer sb = new StringBuffer();
        sb.append(NEWLINES).append(NEWLINES);
        sb.append("HostName").append(COLON).append(HOSTNAME).append(NEWLINES);
        sb.append("RequestTime").append(COLON).append(getRequestTime(request)).append(NEWLINES);
        sb.append("ErrorTime").append(COLON).append(LocalDateTime.now().toString()).append(NEWLINES);
        sb.append("Error").append(COLON).append(Throwables.getRootCause(e).getMessage()).append(NEWLINES);
        sb.append("Resources").append(COLON).append(request.getMethod()).append(" ").append(request.getRequestURI()).append(NEWLINES);
        sb.append("RequestParameters {").append(NEWLINES);
        request.getParameterMap().keySet().stream().forEach(k -> sb.append("\t").append(k).append(COLON).append(request.getParameter(k.toString())).append(NEWLINES));
        sb.append("}").append(NEWLINES);
        sb.append("RequestHeaders {").append(NEWLINES);
        Enumeration headers = request.getHeaderNames();
        while (headers.hasMoreElements()) {
            String key = headers.nextElement().toString();
            sb.append("\t").append(key).append(":").append(request.getHeader(key)).append(NEWLINES);
        }
        sb.append("}").append(NEWLINES);
        sb.append(NEWLINES);
        sb.append(Throwables.getStackTraceAsString(e));
        return sb.toString();
    }

    private static String getRequestTime(HttpServletRequest request) {
        Object requestTime = request.getAttribute(Constants.REQUEST_TIME);
        if (requestTime == null) return "";
        LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) requestTime), TimeUtil.DEFAULT_TIMEZONE);
        return localDateTime.toString();
    }
}
