package com.tonthepduybao.api.model.debt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import org.hibernate.validator.constraints.Length;

import java.util.List;

/**
 * DebtCreateForm
 *
 * @author khal
 * @since 2023/07/09
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DebtForm {

        @NotNull
        private String id;

        @NotBlank @Length(max = 8)
        String date;

        @NotEmpty
        List<Long> propertyIds;

        @NotBlank @Length(max = 50)
        String type;

        @NotNull
        Long customerId;

        @NotEmpty
        List<DebtDetailForm> items;

        List<Long> deletedItems;

}
