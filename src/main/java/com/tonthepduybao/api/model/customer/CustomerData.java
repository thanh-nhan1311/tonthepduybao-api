package com.tonthepduybao.api.model.customer;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * CustomerData
 *
 * @author khal
 * @since 2023/07/10
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CustomerData {

    private Long id;
    private String name;
    private String phone;
    private String email;
    private String address;
    private String type;
    private String createdBy;
    private String updatedBy;
    private String createdAt;
    private String updatedAt;

}
