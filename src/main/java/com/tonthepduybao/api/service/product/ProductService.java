/*package com.tonthepduybao.api.service.product;

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
/* interface ProductService {

    void create(ProductForm form);

    void createAll(ProductListForm listForm);

    void delete(Long id);

    void deleteAll(String type);

    PagingWrapper getAll(String search, List<String> type, List<Long> branchId, int page, int pageSize);

    ProductDetailData get(Long id);

    List<ProductOptionData> getAllOption(Long branchId);

}*/
package com.tonthepduybao.api.service.product;

import com.tonthepduybao.api.model.PagingWrapper;
import com.tonthepduybao.api.model.product.ProductDetailData;
import com.tonthepduybao.api.model.product.ProductForm;
import com.tonthepduybao.api.model.product.ProductListForm;
import com.tonthepduybao.api.model.product.ProductOptionData;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface ProductService {

    // Existing methods for creating, deleting, getting products...
    void create(ProductForm form);

    void createAll(ProductListForm listForm);

    void delete(Long id);

    void deleteAll(String type);

    PagingWrapper getAll(String search, List<String> type, List<Long> branchId, int page, int pageSize);

    ProductDetailData get(Long id);

    List<ProductOptionData> getAllOption(Long branchId);

    // New methods for handling bulk upload via Excel
    ResponseEntity<ByteArrayResource> downloadTemplate(); // Download the template file for bulk upload

    List<String> uploadFromFile(MultipartFile file); // Upload and process the Excel file
}

