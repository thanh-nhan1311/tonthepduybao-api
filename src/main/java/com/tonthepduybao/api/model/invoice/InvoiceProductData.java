package com.tonthepduybao.api.model.invoice;

import com.tonthepduybao.api.model.product.ProductOptionData;
import lombok.*;

import java.math.BigDecimal;

/**
 * DebtData
 *
 * @author khal
 * @since 2023/07/10
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductData {

    private Long id;
    private BigDecimal unitPrice;
    private int quantity;
    private double size;
    private double sizeCalculator;
    private ProductOptionData product;

}
