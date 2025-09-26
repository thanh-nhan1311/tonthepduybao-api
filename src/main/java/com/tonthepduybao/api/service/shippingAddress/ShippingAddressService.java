package com.tonthepduybao.api.service.shippingAddress;

import com.tonthepduybao.api.model.shippingAddress.ShippingAddressData;
import com.tonthepduybao.api.model.shippingAddress.ShippingAddressForm;

import java.util.List;

/**
 * ShippingAddress
 *
 * @author jackerbit77
 * @since 2024/03/09
 */
public interface ShippingAddressService {

    void upsert(ShippingAddressForm form);

    void updateDefault(Long id, Long customerId);

    void delete(Long id);

    List<ShippingAddressData> getAll(Long customerId);


}
