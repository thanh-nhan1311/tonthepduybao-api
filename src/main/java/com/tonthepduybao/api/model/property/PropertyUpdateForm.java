package com.tonthepduybao.api.model.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * PropertyUpdateForm
 *
 * @author khal
 * @since 2023/05/14
 */
public record PropertyUpdateForm(
        @NotNull
        Long id,

        @NotBlank @Length(max = 500)
        String name,

        @NotBlank @Length(max = 50)
        String type,

        @NotEmpty
        PropertyDetailUpdateForm[] properties
) {}
