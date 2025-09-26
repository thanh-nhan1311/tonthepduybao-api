package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.InvoiceShippingAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * InvoiceShippingAddressRepository
 *
 * @author jackerbit77
 * @since 2024/03/10
 */
@Repository
public interface InvoiceShippingAddressRepository extends JpaRepository<InvoiceShippingAddress, Long> {
}
