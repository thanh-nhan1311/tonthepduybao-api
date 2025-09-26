package com.tonthepduybao.api.model.debt;

import com.tonthepduybao.api.model.PagingWrapper;
import lombok.*;

import java.math.BigDecimal;

/**
 * DebtListData
 *
 * @author khal
 * @since 2023/08/21
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtListData {

    private PagingWrapper allDebt;
    private BigDecimal totalPrice;

}
