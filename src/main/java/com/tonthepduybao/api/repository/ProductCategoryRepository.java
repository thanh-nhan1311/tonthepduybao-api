package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.ProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * ProductCategoryRepository
 *
 * @author khal
 * @since 2023/07/23
 */
@Repository
public interface ProductCategoryRepository extends JpaRepository<ProductCategory, Long> {

    @Query("SELECT tpc FROM ProductCategory tpc " +
            "WHERE tpc.name ILIKE :search " +
            "ORDER BY tpc.updatedAt DESC")
    List<ProductCategory> searchAll(@Param("search") String search);

}
