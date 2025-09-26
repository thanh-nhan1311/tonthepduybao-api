package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.SiteCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * SiteCategoryRepository
 *
 * @author khal
 * @since 2023/03/12
 */
public interface SiteCategoryRepository extends JpaRepository<SiteCategory, Long> {

    @Query(value = "SELECT tsc " +
            "FROM SiteCategory tsc " +
            "WHERE " +
            "   LOWER(tsc.name) LIKE :search " +
            "   OR LOWER(tsc.seoUrl) LIKE :search " +
            "ORDER BY tsc.updatedAt DESC ")
    List<SiteCategory> searchCategory(@Param("search") String search);

}
