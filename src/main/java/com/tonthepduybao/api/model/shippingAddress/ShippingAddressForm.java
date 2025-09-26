package com.tonthepduybao.api.model.shippingAddress;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * ShippingAddressForm
 *
 * @author khal
 * @since 2023/03/04
 */
public record ShippingAddressForm(

    Long id,

    @NotBlank @Length(max = 500)
    String name,

    @NotBlank @Length(max = 20)
    String phone,

    @NotBlank @Length(max = 2000)
    String address,

    boolean defaultAddress,

    Long customerId

) {}
