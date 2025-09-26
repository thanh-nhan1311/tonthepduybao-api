package com.tonthepduybao.api.model.productCategory;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ProductCategoryData
 *
 * @author khal
 * @since 2023/05/14
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductCategoryData {

    private Long id;
    private String name;

    private String createdBy;
    private String updatedBy;

    private String createdAt;
    private String updatedAt;

}
