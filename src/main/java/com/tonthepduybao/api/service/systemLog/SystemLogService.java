package com.tonthepduybao.api.service.systemLog;

import com.tonthepduybao.api.entity.SystemLog;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;

/**
 * SystemLog
 *
 * @author khal
 * @since 2023/07/15
 */
public interface SystemLogService {

    void save(HttpServletRequest request, final String screenUrl);

    ResponseEntity<Resource> download(Integer day, String type);

}
