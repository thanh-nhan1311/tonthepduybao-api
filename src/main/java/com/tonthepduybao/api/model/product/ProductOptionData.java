package com.tonthepduybao.api.model.product;

import com.tonthepduybao.api.model.branch.BranchModel;
import lombok.*;

/**
 * ProductOptionData
 *
 * @author khal
 * @since 2023/07/26
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductOptionData {

    private Long id;
    private String name;
    private Double quantity;

}
