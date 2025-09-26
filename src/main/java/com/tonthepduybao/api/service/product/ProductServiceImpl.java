package com.tonthepduybao.api.service.product;

import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.Branch;
import com.tonthepduybao.api.entity.Product;
import com.tonthepduybao.api.entity.ProductPropertyDetail;
import com.tonthepduybao.api.entity.PropertyDetail;
import com.tonthepduybao.api.entity.enumeration.EProductStatus;
import com.tonthepduybao.api.entity.enumeration.EType;
import com.tonthepduybao.api.mapper.ProductMapper;
import com.tonthepduybao.api.model.PagingWrapper;
import com.tonthepduybao.api.model.branch.BranchModel;
import com.tonthepduybao.api.model.product.*;
import com.tonthepduybao.api.model.property.PropertyDetailData;
import com.tonthepduybao.api.repository.*;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * ProductServiceImpl
 *
 * @author khal
 * @since 2023/07/23
 */
@RequiredArgsConstructor
@Service
public class ProductServiceImpl implements ProductService {

    private final MessageHelper messageHelper;

    private final ProductMapper productMapper;

    private final UserRepository userRepository;
    private final BranchRepository branchRepository;
    private final ProductRepository productRepository;
    private final PropertyDetailRepository propertyDetailRepository;
    private final ProductPropertyDetailRepository productPropertyDetailRepository;

    @Override
    public void create(final ProductForm form) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);
        String now = TimeUtils.nowStr();

        Branch branch = getBranch(form.branch());

        Product product = Product.builder()
                .name(form.name())
                .date(form.date())
                .quantity(form.quantity())
                .size(form.size())
                .sizeCalculator(form.sizeCalculator())
                .branch(branch)
                .type(EType.valueOf(form.type()))
                .status(EProductStatus.ACTIVE)
                .createdAt(now)
                .createdBy(currentUserId)
                .updatedAt(now)
                .updatedBy(currentUserId)
                .parent(form.parent())
                .build();
        Product savedProduct = productRepository.save(product);

        // Save product properties
        saveProductPropertyDetail(form.properties(), savedProduct);
    }

    @Override
    public void createAll(final ProductListForm listForm) {
        listForm.data().forEach(this::create);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(final Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("Sản phẩm với ID =", id));

        productPropertyDetailRepository.deleteAllByProduct(product);
        productRepository.delete(product);
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void deleteAll(final String type) {
        if (type.equals("ALL")) {
            productPropertyDetailRepository.deleteAll();
            productRepository.deleteAll();
        } else {
            List<Product> products = productRepository.findAllByType(EType.valueOf(type));
            products.forEach(productPropertyDetailRepository::deleteAllByProduct);
            productRepository.deleteAll(products);
        }
    }

    @Override
    public PagingWrapper getAll(final String search, final List<String> type, final List<Long> branchId, final int page, final int pageSize) {
        String searchParam = "%" + search.toLowerCase() + "%";
        String status = EProductStatus.ACTIVE.name();

        int offset = (page - 1) * pageSize;
        long totalItems = productMapper.countProduct(status, searchParam, type, branchId);
        int totalPages = (int) Math.ceil(totalItems / (pageSize * 1.0));
        List<ProductData> productDatasets = productMapper.selectProduct(status, searchParam, type, branchId, pageSize, offset)
                .stream()
                .map(item -> {
                    Branch branch = getBranch(item.getBranchId());
                    String parent = Objects.isNull(item.getParent()) ? "" : productRepository.getNameById(item.getParent());

                    String createdBy = userRepository.getFullNameById(item.getCreatedBy());
                    String updatedBy = userRepository.getFullNameById(item.getUpdatedBy());

                    ProductData productData = DataBuilder.to(item, ProductData.class);
                    productData.setCreatedBy(createdBy);
                    productData.setUpdatedBy(updatedBy);
                    productData.setParent(parent);
                    productData.setBranch(DataBuilder.to(branch, BranchModel.class));

                    return productData;
                }).toList();

        return PagingWrapper.builder()
                .data(productDatasets)
                .page(page)
                .pageSize(pageSize)
                .totalItems(totalItems)
                .totalPages(totalPages)
                .build();
    }

    @Override
    public ProductDetailData get(final Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(messageHelper.buildDataNotFound("Sản phẩm với ID =", id));

        String createdBy = userRepository.getFullNameById(product.getCreatedBy());
        String updatedBy = userRepository.getFullNameById(product.getUpdatedBy());

        List<PropertyDetailData> properties = product.getProductPropertyDetails().stream()
                .map(item -> PropertyDetailData.builder()
                        .id(item.getPropertyDetail().getId())
                        .name(item.getPropertyDetail().getName())
                        .build())
                .toList();

        ProductDetailData data = DataBuilder.to(product, ProductDetailData.class);
        data.setProperties(properties);
        data.setCreatedBy(createdBy);
        data.setUpdatedBy(updatedBy);
        data.setType(product.getType().name());
        data.setStatus(product.getStatus().name());
        data.setBranch(DataBuilder.to(product.getBranch(), BranchModel.class));

        return data;
    }

    @Override
    public List<ProductOptionData> getAllOption(Long branchId) {
        Branch branch = getBranch(branchId);
        return productRepository.findAllByBranch(branch)
                .stream().map(item -> ProductOptionData.builder()
                        .id(item.getId())
                        .name(item.getName())
                        .quantity(item.getQuantity())
                .build())
                .toList();
    }

    // Private functions
    private Branch getBranch(final Long branchId) {
        return branchRepository.findById(branchId)
                .orElseThrow(messageHelper.buildDataNotFound("Chi nhánh với ID =", branchId));
    }

    private void saveProductPropertyDetail(final Map<Long, Long> properties, final Product savedProduct) {
        productPropertyDetailRepository.deleteAllByProduct(savedProduct);

        for (var property : properties.entrySet()) {
            Long propertyDetailId = property.getValue();
            if (Objects.nonNull(propertyDetailId)) {
                PropertyDetail propertyDetail = propertyDetailRepository.findById(propertyDetailId)
                        .orElseThrow(messageHelper.buildDataNotFound("Giá trị thuộc tính với ID =", propertyDetailId));

                ProductPropertyDetail productPropertyDetail = ProductPropertyDetail.builder()
                        .product(savedProduct)
                        .propertyDetail(propertyDetail)
                        .build();
                productPropertyDetailRepository.saveAndFlush(productPropertyDetail);
            }
        }
    }

}
