package com.tonthepduybao.api.app.logging;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.io.IOUtils;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * AppLogger
 *
 * @author khal
 * @since 2022/10/02
 */
public class Logger {

    public static final org.slf4j.Logger ACCESS_LOGGER = LoggerFactory.getLogger("accessLogger");
    public static final org.slf4j.Logger DEBUG_LOGGER = LoggerFactory.getLogger("debugLogger");
    public static final org.slf4j.Logger ERROR_LOGGER = LoggerFactory.getLogger("errorLogger");

    public static boolean isAccessLoggerEnable(final String uri) {
        return ACCESS_LOGGER.isInfoEnabled() && StringUtils.hasLength(uri);
    }

    public static void access(final HttpServletRequest request, final HttpServletResponse response) throws IOException {
        StringBuilder strBuff = new StringBuilder();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dt = new Date();
        long startTime = dt.getTime();

        String method = request.getMethod();
        String contentType = request.getContentType();
        String user = request.getHeader("x-user-id");

        String queryStr = "";
        if ("GET".equals(method) || "DELETE".equals(method))
            queryStr = request.getQueryString();
        else if (("POST".equals(method) || "PUT".equals(method))
                && StringUtils.hasLength(contentType)
                && contentType.contains("application/json"))
            queryStr = IOUtils.toString(request.getReader());
        queryStr = StringUtils.hasLength(queryStr) ? queryStr : "-";

        String localIpAddr = request.getLocalAddr();
        String ipAddr = StringUtils.hasLength(localIpAddr) ? localIpAddr : request.getHeader("x-real-ip");

        strBuff.append("startTime:").append(format.format(dt))
                .append("\t").append("user:").append(user)
                .append("\t").append("ip:").append(ipAddr)
                .append("\t").append("method:").append(method)
                .append("\t").append("uri:").append(request.getRequestURI())
                .append("\t").append("params:").append(queryStr)
                .append("\t").append("processTime:");

        // calculate time on each request is HTTP GET has content type
        double processTime = System.currentTimeMillis() - startTime;
        processTime = processTime / (1000 * 1.0);
        strBuff.append(processTime);

        strBuff.append("\t").append("status:").append(response.getStatus());

        // log access
        ACCESS_LOGGER.info(strBuff.toString());
    }

}
