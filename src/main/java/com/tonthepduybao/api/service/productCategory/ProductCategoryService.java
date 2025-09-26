package com.tonthepduybao.api.service.productCategory;

import com.tonthepduybao.api.model.productCategory.ProductCategoryData;
import com.tonthepduybao.api.model.productCategory.ProductCategoryForm;

import java.util.List;

/**
 * ProductCategoryService
 *
 * @author khal
 * @since 2023/07/23
 */
public interface ProductCategoryService {

    void upsert(ProductCategoryForm form);

    List<ProductCategoryData> getAll(String search);


}
