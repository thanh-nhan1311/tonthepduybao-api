package com.tonthepduybao.api.model.product;

import com.tonthepduybao.api.model.branch.BranchModel;
import com.tonthepduybao.api.model.property.PropertyDetailData;
import lombok.*;

import java.util.List;

/**
 * ProductData
 *
 * @author khal
 * @since 2023/07/26
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductDetailData {

    private Long id;
    private String name;
    private String type;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String createdAt;
    private String updatedAt;
    private String parent;

    private String date;
    private Double quantity;
    private Double size;
    private Double sizeCalculator;
    private BranchModel branch;

    private List<PropertyDetailData> properties;

}
