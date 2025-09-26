package com.tonthepduybao.api.model.product;

import jakarta.validation.constraints.NotEmpty;

import java.util.List;

/**
 * ProductForm
 *
 * @author khal
 * @since 2023/07/23
 */
public record ProductListForm(

        @NotEmpty
        List<ProductForm> data

) { }
