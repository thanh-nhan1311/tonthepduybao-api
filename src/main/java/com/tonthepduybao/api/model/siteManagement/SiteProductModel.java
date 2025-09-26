package com.tonthepduybao.api.model.siteManagement;

import com.tonthepduybao.api.entity.enumeration.ESiteProductStatus;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * SiteProductModel
 *
 * @author khal
 * @since 2023/03/12
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SiteProductModel {

    private Long id;

    @NotBlank
    @Length(max = 500)
    private String name;

    private String seoUrl;

    @Length(max = 500)
    private String summary;
    private String description;

    @Length(max = 2048)
    private String thumbnail;

    private ESiteProductStatus status;

    private String createdAt;
    private String updatedAt;



}
