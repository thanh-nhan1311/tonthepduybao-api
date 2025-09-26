package com.tonthepduybao.api.service.invoice;

import com.tonthepduybao.api.model.invoice.InvoiceData;
import com.tonthepduybao.api.model.invoice.InvoiceForm;
import com.tonthepduybao.api.model.invoice.InvoiceListData;

import java.util.List;

/**
 * InvoiceService
 *
 * @author jackerbit77
 * @since 2024/03/01
 */
public interface InvoiceService {

    void create(InvoiceForm form);

    void delete(String id);

    InvoiceData get(String id);

    InvoiceListData getAll(Long search, String fromDate, String toDate, List<Long> branchId, List<Long> customerId, int page, int pageSize);

}
