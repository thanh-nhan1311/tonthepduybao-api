package com.tonthepduybao.api.service.property;

import com.tonthepduybao.api.app.helper.MessageHelper;
import com.tonthepduybao.api.app.utils.TimeUtils;
import com.tonthepduybao.api.entity.Property;
import com.tonthepduybao.api.entity.PropertyDetail;
import com.tonthepduybao.api.entity.enumeration.EType;
import com.tonthepduybao.api.model.property.PropertyCreateForm;
import com.tonthepduybao.api.model.property.PropertyData;
import com.tonthepduybao.api.model.property.PropertyDetailData;
import com.tonthepduybao.api.model.property.PropertyUpdateForm;
import com.tonthepduybao.api.repository.*;
import com.tonthepduybao.api.security.utils.SecurityUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;

/**
 * PropertyServiceImpl
 *
 * @author khal
 * @since 2023/05/14
 */
@Service
@RequiredArgsConstructor
public class PropertyServiceImpl implements PropertyService {

    private final MessageHelper messageHelper;

    private final UserRepository userRepository;
    private final PropertyRepository propertyRepository;
    private final PropertyDetailRepository propertyDetailRepository;
    private final DebtPropertyRepository debtPropertyRepository;
    private final ProductPropertyDetailRepository productPropertyDetailRepository;
    private final DebtDetailPropertyDetailRepository debtDetailPropertyDetailRepository;

    @Override
    public List<PropertyData> getAll(final String search, final String type) {
        return propertyRepository.searchByName("%" + search.toLowerCase() + "%", EType.valueOf(type))
                .stream()
                .map(property -> {
                    List<PropertyDetailData> items = property.getPropertyDetails()
                            .stream()
                            .map(item -> {
                                boolean used = isPropertyDetailInUsed(item);
                                return PropertyDetailData.builder()
                                        .id(item.getId())
                                        .name(item.getName())
                                        .used(used)
                                        .build();
                            }).toList();

                    String createdBy = userRepository.getFullNameById(property.getCreatedBy());
                    String updatedBy = userRepository.getFullNameById(property.getUpdatedBy());

                    boolean used = debtPropertyRepository.existsByProperty(property);
                    return PropertyData.builder()
                            .id(property.getId())
                            .name(property.getName())
                            .createdAt(property.getCreatedAt())
                            .createdBy(createdBy)
                            .updatedAt(property.getUpdatedAt())
                            .updatedBy(updatedBy)
                            .used(used)
                            .orderBy(property.getOrderBy())
                            .type(property.getType().name())
                            .items(items)
                            .build();
                }).toList();
    }

    @Override
    public List<PropertyData> getAllByType(final String type) {
        return propertyRepository.findAllByTypeOrderByUpdatedAtDesc(EType.valueOf(type))
                .stream()
                .map(property -> {
                    List<PropertyDetailData> items = property.getPropertyDetails()
                            .stream()
                            .map(item -> PropertyDetailData.builder()
                                    .id(item.getId())
                                    .name(item.getName())
                                    .build())
                            .toList();

                    return PropertyData.builder()
                            .id(property.getId())
                            .name(property.getName())
                            .orderBy(property.getOrderBy())
                            .type(property.getType().name())
                            .items(items)
                            .build();
                }).toList();
    }

    @Override
    public void create(final PropertyCreateForm form) {
        Long currentUserId = SecurityUtils.getCurrentUserId(true);
        Property property = Property.builder()
                .name(form.name())
                .type(EType.valueOf(form.type()))
                .createdBy(currentUserId)
                .createdAt(TimeUtils.nowStr())
                .updatedBy(currentUserId)
                .updatedAt(TimeUtils.nowStr())
                .build();
        Property savedProperty = propertyRepository.saveAndFlush(property);

        List.of(form.properties()).forEach(item -> {
            PropertyDetail propertyDetail =  PropertyDetail.builder()
                    .name(item)
                    .property(savedProperty)
                    .build();
            propertyDetailRepository.saveAndFlush(propertyDetail);
        });
    }


    @Override
    public void update(final PropertyUpdateForm form) {
        Long propertyId = form.id();
        Long currentUserId = SecurityUtils.getCurrentUserId(true);

        Property property = propertyRepository.findById(propertyId)
                .orElseThrow(messageHelper.buildDataNotFound("Thuộc tính với ID =", propertyId));
        property.setName(form.name());
        property.setType(EType.valueOf(form.type()));
        property.setUpdatedBy(currentUserId);
        property.setUpdatedAt(TimeUtils.nowStr());

        Property savedProperty = propertyRepository.saveAndFlush(property);
        List.of(form.properties()).forEach(item -> {
            Long propertyDetailId = item.id();
            PropertyDetail propertyDetail = new PropertyDetail();

            if (Objects.nonNull(propertyDetailId)) {
                propertyDetail = propertyDetailRepository.findById(propertyDetailId)
                        .orElseThrow(messageHelper.buildDataNotFound("Giá trị thuộc tính với ID =", propertyDetailId));

                if (item.deleted() && !isPropertyDetailInUsed(propertyDetail)) {
                    propertyDetailRepository.delete(propertyDetail);
                } else {
                    propertyDetail.setName(item.name());
                    propertyDetailRepository.saveAndFlush(propertyDetail);
                }
            } else {
                propertyDetail.setName(item.name());
                propertyDetail.setProperty(savedProperty);
                propertyDetailRepository.saveAndFlush(propertyDetail);
            }
        });
    }

    @Override
    @Transactional(rollbackFor = Throwable.class)
    public void delete(Long id) {
        Property property = propertyRepository.findById(id)
                .orElse(null);

        if (Objects.nonNull(property) && !debtPropertyRepository.existsByProperty(property)) {
            propertyDetailRepository.deleteAllByProperty(property);
            propertyRepository.delete(property);
        }
    }

    // Private
    private boolean isPropertyDetailInUsed(final PropertyDetail propertyDetail) {
        return debtDetailPropertyDetailRepository.existsByPropertyDetail(propertyDetail)
                || productPropertyDetailRepository.existsByPropertyDetail(propertyDetail);
    }
}
