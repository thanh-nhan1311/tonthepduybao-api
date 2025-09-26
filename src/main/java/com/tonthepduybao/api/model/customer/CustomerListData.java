package com.tonthepduybao.api.model.customer;

import lombok.*;

import java.util.List;

/**
 * CustomerListData
 *
 * @author jackerbit77
 * @since 2024/03/09
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerListData {

    private List<CustomerData> customers;
    private long totalCustomer;
    private long totalSupplier;
    private long totalDeleted;

}
