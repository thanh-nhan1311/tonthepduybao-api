package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.service.systemLog.SystemLogService;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * SystemLogController
 *
 * @author khal
 * @since 2023/07/15
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/log")
public class SystemLogController {

    private final SystemLogService systemLogService;

    @GetMapping(value = "/download")
    public ResponseEntity<Resource> download(@RequestParam("day") @NotNull Integer day,
                                             @RequestParam("type") @NotBlank String type) {
        return systemLogService.download(day, type);
    }

}
