package com.tonthepduybao.api.model.productCategory;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * ProductCategoryForm
 *
 * @author khal
 * @since 2023/07/23
 */
public record ProductCategoryForm (
        Long id,

        @NotBlank @Length(max = 500)
        String name
) {}
