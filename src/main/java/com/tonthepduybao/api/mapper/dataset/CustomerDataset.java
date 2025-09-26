package com.tonthepduybao.api.mapper.dataset;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CustomerDataset
 *
 * @author khal
 * @since 2023/07/10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerDataset {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String type;
    private Long createdBy;
    private Long updatedBy;
    private String createdAt;
    private String updatedAt;

}
