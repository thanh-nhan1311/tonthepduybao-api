package com.tonthepduybao.api.model.user;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

/**
 * UserUpdateForm
 *
 * @author khal
 * @since 2023/07/15
 */
public record UserUpdateForm(

        @NotBlank @Length(max = 500)
        String fullName,

        @NotBlank @Length(max = 20)
        String phone,

        @NotBlank @Length(max = 320)
        String email,

        @NotBlank @Length(max = 1000)
        String address

) {}
