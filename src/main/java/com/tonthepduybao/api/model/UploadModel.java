package com.tonthepduybao.api.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;

/**
 * UploadModel
 *
 * @author khal
 * @since 2023/04/04
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UploadModel {

    @NotBlank
    private String directory;

    @NotEmpty
    private MultipartFile[] files;

}
