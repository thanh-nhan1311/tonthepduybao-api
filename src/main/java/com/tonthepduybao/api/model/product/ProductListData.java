package com.tonthepduybao.api.model.product;

import com.tonthepduybao.api.model.PagingWrapper;
import lombok.*;

import java.math.BigDecimal;

/**
 * ProductListData
 *
 * @author khal
 * @since 2023/08/21
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ProductListData {

    private PagingWrapper allProduct; // Changed from allInvoice to allProduct
    private BigDecimal totalPrice; // Retained, assuming total price is relevant for products as well

}
