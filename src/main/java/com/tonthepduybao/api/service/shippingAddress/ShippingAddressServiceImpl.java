package com.tonthepduybao.api.service.shippingAddress;

import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.Customer;
import com.tonthepduybao.api.entity.ShippingAddress;
import com.tonthepduybao.api.model.shippingAddress.ShippingAddressData;
import com.tonthepduybao.api.model.shippingAddress.ShippingAddressForm;
import com.tonthepduybao.api.repository.CustomerRepository;
import com.tonthepduybao.api.repository.ShippingAddressRepository;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * ShippingAddressServiceImpl
 *
 * @author jackerbit77
 * @since 2024/03/09
 */
@Service
@RequiredArgsConstructor
public class ShippingAddressServiceImpl implements ShippingAddressService {

    private final MessageHelper messageHelper;

    private final CustomerRepository customerRepository;
    private final ShippingAddressRepository shippingAddressRepository;

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void upsert(final ShippingAddressForm form) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);
        Customer customer = getCustomer(form.customerId());

        Long id = form.id();
        String now = TimeUtils.nowStr();

        ShippingAddress shippingAddress = new ShippingAddress();
        if (Objects.nonNull(id))
            shippingAddress = getShippingAddress(id);
        else {
            shippingAddress.setCreatedAt(now);
            shippingAddress.setCreatedBy(currentUserId);
        }

        shippingAddress.setName(form.name());
        shippingAddress.setPhone(form.phone());
        shippingAddress.setAddress(form.address());
        shippingAddress.setDefaultAddress(false);
        shippingAddress.setCustomer(customer);
        shippingAddress.setUpdatedAt(now);
        shippingAddress.setUpdatedBy(currentUserId);
        ShippingAddress savedShippingAddress = shippingAddressRepository.saveAndFlush(shippingAddress);

        if (form.defaultAddress()) setDefault(customer, savedShippingAddress);
    }

    @Override
    public void updateDefault(final Long id, final Long customerId) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);

        Customer customer = getCustomer(customerId);
        ShippingAddress shippingAddress = getShippingAddress(id);
        shippingAddress.setUpdatedAt(TimeUtils.nowStr());
        shippingAddress.setUpdatedBy(currentUserId);
        setDefault(customer, shippingAddress);
    }

    @Override
    @Transactional
    public void delete(final Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);

        ShippingAddress shippingAddress = getShippingAddress(id);
        shippingAddress.setDeleted(true);
        shippingAddress.setUpdatedAt(TimeUtils.nowStr());
        shippingAddress.setUpdatedBy(currentUserId);
        shippingAddressRepository.saveAndFlush(shippingAddress);
    }

    @Override
    public List<ShippingAddressData> getAll(final Long customerId) {
        Customer customer = getCustomer(customerId);

        return shippingAddressRepository.findAllByCustomerAndDeletedOrderByUpdatedAtDesc(customer, false)
                .stream()
                .map(item -> DataBuilder.to(item, ShippingAddressData.class))
                .toList();
    }

    // private function
    private Customer getCustomer(final Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(messageHelper.buildDataNotFound("ID khách hàng", customerId));
    }

    private ShippingAddress getShippingAddress(final Long id) {
        return shippingAddressRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("ID địa chỉ giao hàng", id));
    }

    private void setDefault(final Customer customer, final ShippingAddress shippingAddress) {
        List<ShippingAddress> shippingAddresses = shippingAddressRepository.findAllByCustomerAndDeletedOrderByUpdatedAtDesc(customer, false)
                .stream()
                .peek(item -> item.setDefaultAddress(false))
                .toList();
        shippingAddressRepository.saveAllAndFlush(shippingAddresses);

        shippingAddress.setDefaultAddress(true);
        shippingAddressRepository.saveAndFlush(shippingAddress);
    }
}
