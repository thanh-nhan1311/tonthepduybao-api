package com.tonthepduybao.api.model.shippingAddress;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * ShippingAddressData
 *
 * @author jackerbit77
 * @since 2024/03/09
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShippingAddressData {

    private Long id;
    private String name;
    private String phone;
    private boolean defaultAddress;
    private String address;

}
