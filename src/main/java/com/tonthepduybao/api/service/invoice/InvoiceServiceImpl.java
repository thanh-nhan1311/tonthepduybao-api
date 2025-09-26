package com.tonthepduybao.api.service.invoice;

import com.tonthepduybao.api.app.exception.model.ExistenceException;
import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.*;
import com.tonthepduybao.api.entity.enumeration.EInvoiceStatus;
import com.tonthepduybao.api.mapper.InvoiceMapper;
import com.tonthepduybao.api.mapper.dataset.InvoiceDataset;
import com.tonthepduybao.api.model.PagingWrapper;
import com.tonthepduybao.api.model.invoice.*;
import com.tonthepduybao.api.model.product.ProductOptionData;
import com.tonthepduybao.api.model.shippingAddress.ShippingAddressData;
import com.tonthepduybao.api.repository.*;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

/**
 * InvoiceServiceImpl
 *
 * @author jackerbit77
 * @since 2024/03/01
 */
@RequiredArgsConstructor
@Service
public class InvoiceServiceImpl implements InvoiceService {

    private final MessageHelper messageHelper;

    private final InvoiceMapper invoiceMapper;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final BranchRepository branchRepository;
    private final InvoiceRepository invoiceRepository;
    private final CustomerRepository customerRepository;
    private final ProductInvoiceRepository productInvoiceRepository;
    private final ShippingAddressRepository shippingAddressRepository;
    private final InvoiceShippingAddressRepository invoiceShippingAddressRepository;

    @Override
    public void create(final InvoiceForm form) {
        String id = form.getId();
        if (invoiceRepository.existsById(id))
            throw new ExistenceException(List.of("Số hoá đơn ", id));

        Long currentUserId = SecurityUtils.getCurrentUserId(true);

        Branch branch = getBranch(form.getBranchId());
        Customer customer = getCustomer(form.getCustomerId());

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<InvoiceProductForm> items = form.getItems();

        for (InvoiceProductForm item : items) {
            BigDecimal sumTotalPrice = item.getUnitPrice().multiply(new BigDecimal(item.getQuantity()));
            totalPrice = totalPrice.add(sumTotalPrice);
        }

        Invoice invoice = Invoice.builder()
                .id(id)
                .date(form.getDate())
                .note(form.getNote())
                .totalPrice(totalPrice)
                .paidPrice(BigDecimal.ZERO)
                .status(EInvoiceStatus.ACTIVE)
                .createdBy(currentUserId)
                .updatedBy(currentUserId)
                .createdAt(TimeUtils.nowStr())
                .updatedAt(TimeUtils.nowStr())
                .customer(customer)
                .branch(branch)
                .build();
        Invoice savedInvoice = invoiceRepository.saveAndFlush(invoice);

        // save invoice shipping address
        ShippingAddress shippingAddress = getShippingAddress(form.getShippingAddressId());
        InvoiceShippingAddress invoiceShippingAddress = InvoiceShippingAddress.builder()
                .invoice(savedInvoice)
                .shippingAddress(shippingAddress)
                .build();
        invoiceShippingAddressRepository.saveAndFlush(invoiceShippingAddress);

        items.forEach(item -> {
                Product product = productRepository.findById(item.getProductId())
                        .orElseThrow(messageHelper.buildDataNotFound("Sản phẩm với ID =", item.getProductId()));

                product.setQuantity(product.getQuantity() - item.getQuantity());
                productRepository.saveAndFlush(product);

                ProductInvoice productInvoice = ProductInvoice.builder()
                        .unitPrice(item.getUnitPrice())
                        .quantity(item.getQuantity())
                        .size(0)
                        .sizeCalculator(0)
                        .product(product)
                        .invoice(savedInvoice)
                        .build();
                productInvoiceRepository.saveAndFlush(productInvoice);
            });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(final String id) {
        Invoice invoice = getInvoice(id);

        // revert product quantity
        invoice.getProductInvoices().forEach(item -> {
            Product product = item.getProduct();
            product.setQuantity(item.getQuantity() + product.getQuantity());
            productRepository.saveAndFlush(product);
        });

        productInvoiceRepository.deleteAllByInvoice(invoice);
        invoiceRepository.delete(invoice);
    }

    @Override
    public InvoiceData get(final String id) {
        Invoice invoice = getInvoice(id);

        String createdBy = userRepository.getFullNameById(invoice.getCreatedBy());
        String updatedBy = userRepository.getFullNameById(invoice.getUpdatedBy());

        InvoiceData invoiceData = DataBuilder.to(invoice, InvoiceData.class);
        invoiceData.setCreatedBy(createdBy);
        invoiceData.setUpdatedBy(updatedBy);
        invoiceData.setCustomer(invoice.getCustomer());
        invoiceData.setBranch(invoice.getBranch());

        ShippingAddressData shippingAddress = DataBuilder.to(invoice.getInvoiceShippingAddresses().get(0).getShippingAddress(), ShippingAddressData.class);
        invoiceData.setShippingAddress(shippingAddress);

        List<InvoiceProductData> invoiceProducts = invoice.getProductInvoices()
                .stream()
                .map(item -> {
                    InvoiceProductData invoiceProductData = DataBuilder.to(item, InvoiceProductData.class);
                    ProductOptionData product = DataBuilder.to(item.getProduct(), ProductOptionData.class);

                    invoiceProductData.setProduct(product);
                    return invoiceProductData;
                }).toList();
        invoiceData.setInvoiceProducts(invoiceProducts);

        return invoiceData;
    }

    @Override
    public InvoiceListData getAll(final Long search, final String fromDate, final String toDate, final List<Long> branchId, final List<Long> customerId, final int page, final int pageSize) {
        String status = EInvoiceStatus.ACTIVE.name();

        int offset = (page - 1) * pageSize;
        long totalItems = invoiceMapper.countInvoice(status, search, fromDate, toDate, branchId, customerId);
        int totalPages = (int) Math.ceil(totalItems / (pageSize * 1.0));

        List<InvoiceDataset> datasets = invoiceMapper.selectInvoice(status, search, fromDate, toDate, branchId, customerId, pageSize, offset);
        List<InvoiceData> invoiceDatasets = datasets
                .stream()
                .map(item -> {
                    Branch branch = getBranch(item.getBranchId());
                    Customer customer = getCustomer(item.getCustomerId());

                    String createdBy = userRepository.getFullNameById(item.getCreatedBy());
                    String updatedBy = userRepository.getFullNameById(item.getUpdatedBy());

                    InvoiceData invoiceData = DataBuilder.to(item, InvoiceData.class);
                    invoiceData.setCreatedBy(createdBy);
                    invoiceData.setUpdatedBy(updatedBy);
                    invoiceData.setCustomer(customer);
                    invoiceData.setBranch(branch);

                    return invoiceData;
                }).toList();
        BigDecimal totalPrice = datasets.stream()
                .map(InvoiceDataset::getTotalPrice)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        PagingWrapper allInvoice = PagingWrapper.builder()
                .data(invoiceDatasets)
                .page(page)
                .pageSize(pageSize)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .build();

        return InvoiceListData.builder()
                .allInvoice(allInvoice)
                .totalPrice(totalPrice)
                .build();
    }


    // Private
    private Invoice getInvoice(final String id) {
        return invoiceRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("Hoá đơn với ID = ", id));
    }
    private Branch getBranch(final Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(messageHelper.buildDataNotFound("Chi nhánh với ID =", branchId));
    }
    private Customer getCustomer(final Long customerId) {
        return customerRepository.findById(customerId)
                .orElseThrow(messageHelper.buildDataNotFound("Nhà cung cấp với ID =", customerId));
    }

    private ShippingAddress getShippingAddress(final Long id) {
        return shippingAddressRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("ID địa chỉ giao hàng", id));
    }


}
