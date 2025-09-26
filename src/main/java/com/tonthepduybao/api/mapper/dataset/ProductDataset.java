package com.tonthepduybao.api.mapper.dataset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ProductDataset
 *
 * @author khal
 * @since 2023/07/26
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDataset {

    private Long id;
    private String name;
    private String type;
    private String status;
    private Long createdBy;
    private Long updatedBy;
    private String createdAt;
    private String updatedAt;
    private Long parent;
    private Long branchId;
    private Double quantity;

}
