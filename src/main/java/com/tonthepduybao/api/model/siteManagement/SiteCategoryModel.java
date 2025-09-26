package com.tonthepduybao.api.model.siteManagement;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;

/**
 * SiteCategoryModel
 *
 * @author khal
 * @since 2023/03/12
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SiteCategoryModel {

    private Long id;

    @NotBlank
    @Length(max = 500)
    private String name;

    private String seoUrl;
    private long totalProduct;
    private Long parent;

}
