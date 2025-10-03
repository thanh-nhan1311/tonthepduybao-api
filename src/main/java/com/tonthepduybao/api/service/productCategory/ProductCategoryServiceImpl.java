package com.tonthepduybao.api.service.productCategory;

import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.utils.DataBuilder;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.ProductCategory;
import com.tonthepduybao.api.model.productCategory.ProductCategoryData;
import com.tonthepduybao.api.model.productCategory.ProductCategoryForm;
import com.tonthepduybao.api.repository.ProductCategoryRepository;
import com.tonthepduybao.api.repository.UserRepository;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class ProductCategoryServiceImpl implements ProductCategoryService {

    private final MessageHelper messageHelper;
    private final UserRepository userRepository;
    private final ProductCategoryRepository productCategoryRepository;

    @Override
    public void upsert(final ProductCategoryForm form) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);

        Long id = form.id();
        ProductCategory productCategory = new ProductCategory();
        if (Objects.nonNull(id)) {
            productCategory = productCategoryRepository.findById(id)
                    .orElseThrow(messageHelper.buildDataNotFound("danh mục sản phẩm với ID ="));
        } else {
            productCategory.setCreatedBy(currentUserId);
            productCategory.setCreatedAt(TimeUtils.nowStr());
        }

        productCategory.setName(form.name());
        productCategory.setUpdatedAt(TimeUtils.nowStr());
        productCategory.setUpdatedBy(currentUserId);

        productCategoryRepository.saveAndFlush(productCategory);
    }

    @Override
    public List<ProductCategoryData> getAll(final String search) {
        String searchParam = "%" + search.toLowerCase() + "%";
        return productCategoryRepository.searchAll(searchParam)
                .stream()
                .map(item -> {
                    String createdBy = userRepository.getFullNameById(item.getCreatedBy());
                    String updatedBy = userRepository.getFullNameById(item.getUpdatedBy());

                    ProductCategoryData result = DataBuilder.to(item, ProductCategoryData.class);
                    result.setCreatedBy(createdBy);
                    result.setUpdatedBy(updatedBy);

                    return result;
                }).toList();
    }
}
