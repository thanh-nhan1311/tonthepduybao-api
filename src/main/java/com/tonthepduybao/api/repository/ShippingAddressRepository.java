package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Customer;
import com.tonthepduybao.api.entity.ShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * ShippingAddressRepository
 *
 * @author jackerbit77
 * @since 2024/03/09
 */
public interface ShippingAddressRepository extends JpaRepository<ShippingAddress, Long> {

    List<ShippingAddress> findAllByCustomerAndDeletedOrderByUpdatedAtDesc(Customer customer, boolean deleted);

    long countAllByCustomer(Customer customer);

}
