package com.tonthepduybao.api.model.property;

import lombok.*;

/**
 * PropertyDetailData
 *
 * @author khal
 * @since 2023/05/14
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PropertyDetailData {

    public Long id;
    public String name;
    public boolean used;

}
