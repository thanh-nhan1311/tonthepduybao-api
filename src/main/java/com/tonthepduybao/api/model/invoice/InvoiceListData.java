package com.tonthepduybao.api.model.invoice;

import com.tonthepduybao.api.model.PagingWrapper;
import lombok.*;

import java.math.BigDecimal;

/**
 * InvoiceListData
 *
 * @author khal
 * @since 2023/08/21
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceListData {

    private PagingWrapper allInvoice;
    private BigDecimal totalPrice;

}
