package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Property;
import com.tonthepduybao.api.entity.enumeration.EType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

/**
 * PropertyRepository
 *
 * @author khal
 * @since 2023/05/14
 */
public interface PropertyRepository extends JpaRepository<Property, Long> {

    @Query("SELECT tp FROM Property tp " +
            "WHERE" +
            "   tp.name ILIKE :search " +
            "   AND tp.type = :type " +
            "ORDER BY tp.updatedBy DESC ")
    List<Property> searchByName(@Param("search") String search,
                                @Param("type") EType type);

    List<Property> findAllByTypeOrderByUpdatedAtDesc(EType type);

}
