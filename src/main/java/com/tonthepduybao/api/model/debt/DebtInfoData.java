package com.tonthepduybao.api.model.debt;

import com.tonthepduybao.api.entity.Customer;
import com.tonthepduybao.api.model.CommonData;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;

/**
 * DebtInfoData
 *
 * @author khal
 * @since 2023/07/10
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtInfoData {

    private String id;
    private BigDecimal totalPrice;
    private BigDecimal totalUnitPrice;
    private String status;
    private String type;
    private String date;
    private String createdBy;
    private String createdAt;
    private String updatedBy;
    private String updatedAt;
    private Customer customer;
    private List<CommonData> properties;
    private List<DebtDetailData> debtDetails;

}
