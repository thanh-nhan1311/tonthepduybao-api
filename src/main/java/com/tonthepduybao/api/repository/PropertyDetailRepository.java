package com.tonthepduybao.api.repository;

import com.tonthepduybao.api.entity.Property;
import com.tonthepduybao.api.entity.PropertyDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

/**
 * PropertyDetailRepository
 *
 * @author khal
 * @since 2023/05/14
 */
public interface PropertyDetailRepository extends JpaRepository<PropertyDetail, Long> {

    void deleteAllByProperty(Property property);

    List<PropertyDetail> findAllByProperty(Property property);

}
