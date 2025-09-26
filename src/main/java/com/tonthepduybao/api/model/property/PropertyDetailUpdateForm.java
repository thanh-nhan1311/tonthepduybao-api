package com.tonthepduybao.api.model.property;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * PropertyUpdateForm
 *
 * @author khal
 * @since 2023/05/14
 */
public record PropertyDetailUpdateForm(
        Long id,

        @NotBlank @Length(max = 500)
        String name,

        boolean deleted
) {}
