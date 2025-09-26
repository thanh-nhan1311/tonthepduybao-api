package com.tonthepduybao.api.model.product;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

import java.util.List;
import java.util.Map;

/**
 * ProductForm
 *
 * @author khal
 * @since 2023/07/23
 */
public record ProductForm(

        Long id,

        @NotBlank @Length(max = 500)
        String name,

        @NotBlank @Length(max = 50)
        String type,

        Long parent,

        @NotEmpty
        Map<Long, Long> properties,

        @NotBlank @Length(max = 8)
        String date,

        @NotNull
        Double quantity,

        Double size,

        Double sizeCalculator,

        @NotNull
        Long branch

) { }
