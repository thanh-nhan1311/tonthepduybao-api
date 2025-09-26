package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.SiteContact;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * SiteContactRepository
 *
 * @author khal
 * @since 2023/03/05
 */
public interface SiteContactRepository extends JpaRepository<SiteContact, Long> {

    @Query(value = "SELECT tsc " +
            "FROM SiteContact tsc " +
            "WHERE " +
            "   tsc.email ILIKE :search " +
            "   OR tsc.phone ILIKE :search " +
            "   OR tsc.fullName ILIKE :search " +
            "ORDER BY tsc.createdAt DESC")
    List<SiteContact> searchContact(@Param("search") String search);

}
