package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Invoice;
import com.tonthepduybao.api.entity.ProductInvoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductInvoiceRepository
 *
 * @author jackerbit77
 * @since 2024/03/01
 */
public interface ProductInvoiceRepository extends JpaRepository<ProductInvoice, Long> {

    void deleteAllByInvoice(Invoice invoice);

}
