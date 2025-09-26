package com.tonthepduybao.api.model.product;

import com.tonthepduybao.api.entity.Branch;
import com.tonthepduybao.api.model.branch.BranchModel;
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
public class ProductData {

    private Long id;
    private String name;
    private String type;
    private String status;
    private String createdBy;
    private String updatedBy;
    private String createdAt;
    private String updatedAt;
    private String parent;

    private Double quantity;
    private BranchModel branch;

}
