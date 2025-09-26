package com.tonthepduybao.api.model.user;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * UserUpdateForm
 *
 * @author khal
 * @since 2023/07/15
 */
public record UserCreateForm(

        @NotBlank @Length(max = 500)
        String username,

        @NotBlank @Length(max = 100)
        String password,

        @NotNull
        Long branchId,

        @NotNull
        String roleId

) {}
