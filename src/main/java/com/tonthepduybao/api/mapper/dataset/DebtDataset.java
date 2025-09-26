package com.tonthepduybao.api.mapper.dataset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

/**
 * DebtDataset
 *
 * @author khal
 * @since 2023/07/10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtDataset {

    private String id;
    private BigDecimal totalPrice;
    private BigDecimal totalUnitPrice;
    private String status;
    private String type;
    private String date;
    private Long createdBy;
    private Long updatedBy;
    private String createdAt;
    private String updatedAt;

    private Long branchId;
    private Long customerId;

}
