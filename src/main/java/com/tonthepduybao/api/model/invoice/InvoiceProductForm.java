package com.tonthepduybao.api.model.invoice;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * InvoiceProductForm
 *
 * @author jackerbit77
 * @since 2024/03/01
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceProductForm {

    @NotNull
    private Long productId;

    @NotNull
    private Integer quantity;

    @NotNull
    private BigDecimal unitPrice;

}
