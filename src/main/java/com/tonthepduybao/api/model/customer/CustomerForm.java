package com.tonthepduybao.api.model.customer;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * CustomerForm
 *
 * @author khal
 * @since 2023/03/04
 */
public record CustomerForm(

    Long id,

    @NotBlank @Length(max = 500)
    String name,

    @Length(max = 500)
    String phone,

    @Length(max = 320)
    String email,

    @Length(max = 2000)
    String address,

    @NotBlank @Length(max = 50)
    String type

) {}
