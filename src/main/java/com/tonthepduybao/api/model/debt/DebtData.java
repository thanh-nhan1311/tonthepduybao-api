package com.tonthepduybao.api.model.debt;

import com.tonthepduybao.api.entity.Customer;
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
public class DebtData {

    private String id;
    private BigDecimal totalPrice;
    private BigDecimal totalUnitPrice;
    private String type;
    private String date;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private Customer customer;

}
