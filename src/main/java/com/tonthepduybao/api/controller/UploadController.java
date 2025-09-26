package com.tonthepduybao.api.controller;

import com.tonthepduybao.api.app.constant.Constant;
import com.tonthepduybao.api.app.helper.S3Helper;
import com.tonthepduybao.api.model.UploadModel;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * UploadController
 *
 * @author khal
 * @since 2023/04/04
 */
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/ttdb/api/upload")
public class UploadController {

    private final Constant constant;
    private final S3Helper s3Helper;

    @PostMapping(value = "", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public List<String> upload(@Valid @NotNull UploadModel model) {
        return s3Helper.upload(model.getDirectory(), model.getFiles())
                .stream()
                .map(item -> constant.getR2BucketUrl() + item)
                .collect(Collectors.toList());
    }

    @DeleteMapping(value = "")
    public void delete(@RequestParam(value = "path") @NotBlank String path) {
        s3Helper.delete(path.replace(constant.getR2BucketUrl(), ""));
    }

}
