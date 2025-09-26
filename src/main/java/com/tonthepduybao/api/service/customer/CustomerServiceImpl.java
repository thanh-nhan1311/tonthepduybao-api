package com.tonthepduybao.api.service.customer;

import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.Customer;
import com.tonthepduybao.api.entity.enumeration.ECustomerType;
import com.tonthepduybao.api.mapper.CustomerMapper;
import com.tonthepduybao.api.model.CommonData;
import com.tonthepduybao.api.model.PagingWrapper;
import com.tonthepduybao.api.model.customer.CustomerData;
import com.tonthepduybao.api.model.customer.CustomerForm;
import com.tonthepduybao.api.model.customer.CustomerListData;
import com.tonthepduybao.api.repository.CustomerRepository;
import com.tonthepduybao.api.repository.UserRepository;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;

/**
 * CustomerServiceImpl
 *
 * @author khal
 * @since 2023/03/04
 */
@Service
@RequiredArgsConstructor
public class CustomerServiceImpl implements CustomerService {

    private final MessageHelper messageHelper;

    private final UserRepository userRepository;
    private final CustomerMapper customerMapper;
    private final CustomerRepository customerRepository;

    @Override
    public void upsert(final CustomerForm form) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);

        Long id = form.id();
        String now = TimeUtils.nowStr();

        Customer customer = new Customer();
        if (Objects.nonNull(id))
            customer = customerRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("ID khách hàng", id));
        else {
            customer.setCreatedAt(now);
            customer.setCreatedBy(currentUserId);
        }

        customer.setType(ECustomerType.valueOf(form.type()));
        customer.setName(form.name());
        customer.setPhone(form.phone());
        customer.setEmail(form.email());
        customer.setAddress(form.address());
        customer.setUpdatedAt(now);
        customer.setUpdatedBy(currentUserId);
        customer.setDeleted(false);

        customerRepository.saveAndFlush(customer);
    }

    @Override
    public void delete(final Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("ID khách hàng", id));
        customer.setDeleted(true);
        customer.setUpdatedBy(currentUserId);
        customer.setUpdatedAt(TimeUtils.nowStr());
        customerRepository.saveAndFlush(customer);
    }

    @Override
    public void undelete(final Long id) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);

        Customer customer = customerRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("ID khách hàng", id));
        customer.setDeleted(false);
        customer.setUpdatedBy(currentUserId);
        customer.setUpdatedAt(TimeUtils.nowStr());
        customerRepository.saveAndFlush(customer);
    }

    @Override
    public PagingWrapper getAll(final String search, final List<String> type, final boolean deleted, final int page, final int pageSize) {
        String searchParam = "%" + search.toLowerCase() + "%";

        int offset = (page - 1) * pageSize;
        long totalItems = customerMapper.countCustomer(searchParam, false, type);
        int totalPages = (int) Math.ceil(totalItems / (pageSize * 1.0));
        List<CustomerData> customers = customerMapper.selectCustomer(searchParam, type, deleted, pageSize, offset)
                .stream()
                .map(item -> {
                    String createdBy = userRepository.getFullNameById(item.getCreatedBy());
                    String updatedBy = userRepository.getFullNameById(item.getUpdatedBy());

                    CustomerData debtData = DataBuilder.to(item, CustomerData.class);
                    debtData.setCreatedBy(createdBy);
                    debtData.setUpdatedBy(updatedBy);

                    return debtData;
                }).toList();

        long totalCustomer = customerRepository.countAllByType(ECustomerType.CUSTOMER);
        long totalSupplier = customerRepository.countAllByType(ECustomerType.SUPPLIER);
        long totalDeleted = customerRepository.countAllByDeleted(true);

        CustomerListData data = CustomerListData.builder()
                .customers(customers)
                .totalCustomer(totalCustomer)
                .totalSupplier(totalSupplier)
                .totalDeleted(totalDeleted)
                .build();

        return PagingWrapper.builder()
                .data(data)
                .page(page)
                .pageSize(pageSize)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public List<CommonData> getAllOption(final String type) {
        List<Customer> customers;
        if (StringUtils.hasLength(type))
            customers = customerRepository.findAllByType(ECustomerType.valueOf(type));
        else
            customers = customerRepository.findAll();

        return customers.stream()
                .map(item -> CommonData.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .build())
                .toList();
    }
}
