package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * InvoiceRepository
 *
 * @author jackerbit77
 * @since 2024/03/01
 */
public interface InvoiceRepository extends JpaRepository<Invoice, String> {

    boolean existsById(String id);

}
