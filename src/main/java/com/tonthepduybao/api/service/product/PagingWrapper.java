package com.tonthepduybao.api.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PagingWrapper<T> {

    private List<T> data;      // The list of data items for the current page
    private int page;          // The current page number
    private int pageSize;      // The size of each page
    private long totalItems;   // The total number of items (across all pages)
    private int totalPages;    // The total number of pages

}
