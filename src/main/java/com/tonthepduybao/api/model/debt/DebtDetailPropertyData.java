package com.tonthepduybao.api.model.debt;

import com.tonthepduybao.api.entity.Property;
import lombok.*;

/**
 * DebtDetailPropertyData
 *
 * @author khal
 * @since 2023/07/11
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtDetailPropertyData {

    private Long id;
    private String name;
    private Property property;

}
