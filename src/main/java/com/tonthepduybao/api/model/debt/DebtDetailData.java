package com.tonthepduybao.api.model.debt;

import com.tonthepduybao.api.entity.Branch;
import com.tonthepduybao.api.entity.Customer;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DebtDetailData
 *
 * @author khal
 * @since 2023/07/10
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtDetailData {

    private Long id;
    private String name;
    private String note;
    private BigDecimal unitPrice;
    private int quantity;
    private double weight;
    private double avgProportion;
    private BigDecimal totalUnitPrice;
    private BigDecimal totalPrice;
    private Branch branch;
    private List<DebtDetailPropertyData> propertyDetails;

}
