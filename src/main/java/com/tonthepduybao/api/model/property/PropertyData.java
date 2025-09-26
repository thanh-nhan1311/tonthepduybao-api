package com.tonthepduybao.api.model.property;

import lombok.*;

import java.util.List;

/**
 * PropertyData
 *
 * @author khal
 * @since 2023/05/14
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyData {

    private Long id;
    private String name;
    private String createdAt;
    private String createdBy;
    private String updatedAt;
    private String updatedBy;
    private boolean used;
    private int orderBy;
    private String type;
    private List<PropertyDetailData> items;

}
