package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Product;
import com.tonthepduybao.api.entity.ProductPropertyDetail;
import com.tonthepduybao.api.entity.PropertyDetail;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * ProductPropertyDetailRepository
 *
 * @author khal
 * @since 2023/05/14
 */
public interface ProductPropertyDetailRepository extends JpaRepository<ProductPropertyDetail, Long> {

    boolean existsByPropertyDetail(PropertyDetail propertyDetail);

    void deleteAllByProduct(Product product);

}
