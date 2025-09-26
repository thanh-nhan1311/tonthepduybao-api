package com.tonthepduybao.api.service.product;

import com.tonthepduybao.api.model.PagingWrapper;
import com.tonthepduybao.api.model.product.ProductDetailData;
import com.tonthepduybao.api.model.product.ProductForm;
import com.tonthepduybao.api.model.product.ProductListForm;
import com.tonthepduybao.api.model.product.ProductOptionData;

import java.util.List;

/**
 * ProductService
 *
 * @author khal
 * @since 2023/07/23
 */
public interface ProductService {

    void create(ProductForm form);

    void createAll(ProductListForm listForm);

    void delete(Long id);

    void deleteAll(String type);

    PagingWrapper getAll(String search, List<String> type, List<Long> branchId, int page, int pageSize);

    ProductDetailData get(Long id);

    List<ProductOptionData> getAllOption(Long branchId);

}
