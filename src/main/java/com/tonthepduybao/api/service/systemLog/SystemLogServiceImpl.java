package com.tonthepduybao.api.service.systemLog;

import com.tonthepduybao.api.app.exception.model.SystemException;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.SystemLog;
import com.tonthepduybao.api.entity.enumeration.ESystemLogType;
import com.tonthepduybao.api.repository.SystemLogRepository;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

/**
 * SystemLogImpl
 *
 * @author khal
 * @since 2023/07/15
 */
@RequiredArgsConstructor
@Service
public class SystemLogServiceImpl implements SystemLogService {

    private final SystemLogRepository systemLogRepository;

    @Override
    public ResponseEntity<Resource> download(final Integer day, final String type) {
        ResponseEntity<Resource> resource;
        String fromTime = TimeUtils.now()
                .minusDays(day)
                .format(DateTimeFormatter.ofPattern(TimeUtils.DTF_yyyyMMddHHmmss));
        String toTime = TimeUtils.nowStr();

        List<SystemLog> systemLogs = systemLogRepository.searchAll(fromTime, toTime);
        String fileName = "system_log_" + toTime + "." + type.toLowerCase();
        boolean isCSV = ESystemLogType.CSV.equals(ESystemLogType.valueOf(type));

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.CONTENT_DISPOSITION, "attachment;filename=" + fileName);

        try {
            File file = new File(fileName);
            FileWriter fileWriter = new FileWriter(file);

            StringBuilder fileContentBuilder = new StringBuilder();

            if (isCSV)
                fileContentBuilder
                        .append("Time,User_ID,User_Full_Name,Access_Url,Host,IP_Addr,Forwarded_IP_Addr,User_Agent")
                        .append("\n");

            for (SystemLog item : systemLogs)
                fileContentBuilder.append(getRowContent(item, isCSV));

            fileWriter.write(fileContentBuilder.toString());
            fileWriter.close();

            Path path = Paths.get(file.getAbsolutePath());
            ByteArrayResource byteArrayResource = new ByteArrayResource(Files.readAllBytes(path));

            resource = ResponseEntity.ok()
                    .headers(headers)
                    .contentLength(file.length())
                    .contentType(MediaType.APPLICATION_OCTET_STREAM)
                    .body(byteArrayResource);

            if (file.exists())
                file.delete();
        } catch (IOException e) {
            throw new SystemException();
        }

        return resource;
    }

    @Override
    public void save(final HttpServletRequest request, final String screenUrl) {
        Long userId = SecurityUtils.getCurrentUserId(false);

        String requestUrl = request.getRequestURL().toString();
        String requestQueryString = request.getQueryString();
        String accessUrl = StringUtils.hasLength(screenUrl)
                ? screenUrl
                : requestUrl + (StringUtils.hasLength(requestQueryString) ? "?" + requestQueryString : "");
        String host = request.getHeader("host");
        String readIpAddr = request.getHeader("x-real-ip");
        String forwardedIpAddr = request.getHeader("x-forwarded-for");
        String userAgent = request.getHeader("user-agent");
        String locale = request.getHeader("Locale");

        SystemLog systemLog = SystemLog.builder()
                .userId(userId)
                .accessUrl(accessUrl)
                .time(LocalDateTime.now())
                .method(request.getMethod())
                .realIpAddr(readIpAddr)
                .forwardedIpAddr(forwardedIpAddr)
                .userAgent(userAgent)
                .host(host)
                .locale(locale)
                .build();
        systemLogRepository.save(systemLog);
    }

    private String getRowContent(final SystemLog item, final boolean isCSV) {
        String time = item.getTime().format(DateTimeFormatter.ISO_OFFSET_DATE_TIME);
        String userId = Objects.isNull(item.getUserId()) ? "" : String.valueOf(item.getUserId());

        return !isCSV
                    ? "Time=" + time + ";" +
                    "UserID=" + userId + ";" +
                    "UserFullName=" + item.getUserFullName() + ";" +
                    "AccessUrl=" + item.getAccessUrl() + ";" +
                    "Host=" + item.getHost() + ";" +
                    "IPAddr=" + item.getRealIpAddr() + ";" +
                    "ForwardedIPAddr=" + item.getForwardedIpAddr() + ";" +
                    "UserAgent=" + item.getUserAgent() +
                    "\n"
                : time + "," +
                    userId + "," +
                    item.getUserFullName() + "," +
                    item.getAccessUrl() + "," +
                    item.getHost() + "," +
                    item.getRealIpAddr() + "," +
                    item.getForwardedIpAddr() + "," +
                    item.getUserAgent() +
                    "\n";

    }

}
