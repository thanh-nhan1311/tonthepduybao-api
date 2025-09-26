package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.SitePartner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * SitePartnerRepository
 *
 * @author khal
 * @since 2023/04/02
 */
public interface SitePartnerRepository extends JpaRepository<SitePartner, Long> {

    @Query(value = "SELECT tsp " +
            "FROM SitePartner tsp " +
            "WHERE tsp.name ILIKE :search " +
            "ORDER BY tsp.updatedAt DESC ")
    List<SitePartner> searchPartner(@Param("search") String search);

}
