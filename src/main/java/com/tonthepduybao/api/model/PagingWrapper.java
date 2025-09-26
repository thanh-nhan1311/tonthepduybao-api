package com.tonthepduybao.api.model;

import lombok.Builder;

/**
 * PagingWrapper
 *
 * @author khal
 * @since 2022/05/02
 */
@Builder
public record PagingWrapper (
        Object data,
        int page,
        int pageSize,
        int totalPages,
        long totalItems
) { }

