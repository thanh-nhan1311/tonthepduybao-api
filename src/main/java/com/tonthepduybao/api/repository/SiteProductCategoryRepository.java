package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.SiteCategory;
import com.tonthepduybao.api.entity.SiteProductCategory;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * SiteProductCategoryRepository
 *
 * @author khal
 * @since 2023/03/12
 */
public interface SiteProductCategoryRepository extends JpaRepository<SiteProductCategory, Long> {

    long countAllBySiteCategory(SiteCategory siteCategory);
    boolean existsBySiteCategory(SiteCategory siteCategory);

}
