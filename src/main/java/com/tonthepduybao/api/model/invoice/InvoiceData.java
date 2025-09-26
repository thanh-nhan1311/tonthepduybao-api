package com.tonthepduybao.api.model.invoice;

import com.tonthepduybao.api.entity.Branch;
import com.tonthepduybao.api.entity.Customer;
import com.tonthepduybao.api.model.shippingAddress.ShippingAddressData;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

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
public class InvoiceData {

    private String id;
    private String date;
    private String note;
    private BigDecimal totalPrice;
    private BigDecimal paidPrice;
    private String createdBy;
    private String updatedBy;
    private String createdAt;
    private String updatedAt;
    private Branch branch;
    private Customer customer;
    private ShippingAddressData shippingAddress;

    private List<InvoiceProductData> invoiceProducts;

}
