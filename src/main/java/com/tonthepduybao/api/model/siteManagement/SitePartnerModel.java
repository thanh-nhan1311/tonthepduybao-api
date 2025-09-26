package com.tonthepduybao.api.model.siteManagement;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import org.springframework.web.multipart.MultipartFile;

/**
 * SitePartnerModel
 *
 * @author khal
 * @since 2023/04/02
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SitePartnerModel {

    private Long id;

    @NotBlank
    @Length(max = 500)
    private String name;

    private String logo;

    private MultipartFile logoFile;

}
