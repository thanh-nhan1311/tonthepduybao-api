package com.tonthepduybao.api.service.property;

import com.tonthepduybao.api.model.property.PropertyData;
import com.tonthepduybao.api.model.property.PropertyCreateForm;
import com.tonthepduybao.api.model.property.PropertyUpdateForm;

import java.util.List;

/**
 * PropertyService
 *
 * @author khal
 * @since 2023/05/14
 */
public interface PropertyService {

    List<PropertyData> getAll(String search, String type);

    List<PropertyData> getAllByType(String type);

    void create(PropertyCreateForm form);

    void update(PropertyUpdateForm form);

    void delete(Long id);

}
