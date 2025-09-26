package com.tonthepduybao.api.model.property;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import org.hibernate.validator.constraints.Length;

/**
 * PropertyCreateForm
 *
 * @author khal
 * @since 2023/05/14
 */
public record PropertyCreateForm(

        @NotBlank @Length(max = 500)
        String name,

        @NotBlank @Length(max = 50)
        String type,

        @NotEmpty
        String[] properties

) {}
