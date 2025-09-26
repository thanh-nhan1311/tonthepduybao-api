package com.tonthepduybao.api.mapper.dataset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * InvoiceDataset
 *
 * @author khal
 * @since 2023/07/10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDataset {


    private String id;
    private String date;
    private String note;
    private BigDecimal totalPrice;
    private BigDecimal paidPrice;
    private String status;
    private Long createdBy;
    private Long updatedBy;
    private String createdAt;
    private String updatedAt;

    private Long branchId;
    private Long customerId;

}
