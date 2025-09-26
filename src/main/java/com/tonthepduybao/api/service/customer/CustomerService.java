package com.tonthepduybao.api.service.customer;

import com.tonthepduybao.api.model.CommonData;
import com.tonthepduybao.api.model.PagingWrapper;
import com.tonthepduybao.api.model.customer.CustomerForm;

import java.util.List;

/**
 * CustomerService
 *
 * @author khal
 * @since 2023/03/04
 */
public interface CustomerService {

    void upsert(CustomerForm form);

    void delete(Long id);

    void undelete(Long id);

    PagingWrapper getAll(String search, List<String> type, boolean deleted, int page, int pageSize);

    List<CommonData> getAllOption(String type);

}
